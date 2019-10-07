package com.andforce.utils;

import com.andforce.beans.EmojiImage;
import com.andforce.retrofit.managers.DownloadRetrofitManager;
import com.andforce.retrofit.managers.RetrofitManager;
import com.andforce.retrofit.services.DownloadService;
import com.andforce.retrofit.services.EmojiService;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

import java.io.File;
import java.util.List;

public class EmojiDownLoader {

    public void download() {
        RetrofitManager retrofitManager = RetrofitManager.getInstance();
        EmojiService emojiService = retrofitManager.create(EmojiService.class);

        DownloadService downloadService = DownloadRetrofitManager.getInstance().create(DownloadService.class);

        emojiService.apple().

                flatMap(new Function<List<EmojiImage>, Observable<EmojiImage>>() {
                    @Override
                    public Observable<EmojiImage> apply(List<EmojiImage> emojiImages) throws Exception {
                        return Observable.fromIterable(emojiImages);
                    }
                }).

                flatMap(new Function<EmojiImage, ObservableSource<File>>() {
                    @Override
                    public ObservableSource<File> apply(EmojiImage emojiImage) throws Exception {
                        return downloadService.download(emojiImage.getImage()).flatMap(new Function<ResponseBody, ObservableSource<File>>() {
                            @Override
                            public ObservableSource<File> apply(ResponseBody responseBody) throws Exception {

                                final String dir = System.getProperty("user.dir") + File.separator;
                                File file = new File(dir, "images");
                                if (!file.exists()) {
                                    file.mkdirs();
                                }

                                return Observable.just(FileUtils.writeToFile(responseBody.byteStream(),
                                        new File(file, emojiImage.getEmoji() + ".png")));
                            }
                        });
                    }
                }).

                subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
                        System.out.println(file.getAbsoluteFile());
                    }
                });
    }

}
