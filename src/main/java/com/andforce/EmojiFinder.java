package com.andforce;

import com.andforce.beans.EmojiBean;

import java.util.*;

public class EmojiFinder {
    @SuppressWarnings({"rawtypes"})
    private HashMap mMatchHashMap;

    public EmojiFinder(Set<String> emojiSet) {
        mMatchHashMap = makeMatchHashMap(emojiSet);
    }

    //  构建一个DFA算法模型：<br>
    //  中 = {
    //      isEnd = 0
    //      国 = {
    //          isEnd = 1
    //          人 = {
    //              isEnd = 0
    //              民 = { isEnd = 1 }
    //          }
    //          男 = {
    //              isEnd = 0
    //              人 = { isEnd = 1 }
    //          }
    //      }
    //  }
    //
    //  五 = {
    //      isEnd = 0
    //      星 = {
    //          isEnd = 0
    //          红 = {
    //              isEnd = 0
    //              旗 = { isEnd = 1 }
    //          }
    //      }
    //  }
    @SuppressWarnings({"rawtypes", "unchecked"})
    private HashMap makeMatchHashMap(Set<String> emojiSet) {
        //初始化敏感词容器，减少扩容操作
        HashMap hashMap = new HashMap(emojiSet.size());
        String key;
        Map nowMap;
        Map<String, String> newWorMap;
        //迭代keyWordSet
        for (String oneKey : emojiSet) {
            key = oneKey;    //关键字
            nowMap = hashMap;
            for (int i = 0; i < key.length(); i++) {
                //转换成char型
                char keyChar = key.charAt(i);

                //获取
                Object wordMap = nowMap.get(keyChar);

                if (wordMap != null) {
                    //如果存在该key，直接赋值
                    nowMap = (Map) wordMap;
                } else {
                    //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWorMap = new HashMap<>();
                    newWorMap.put("isEnd", "0");     //不是最后一个
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }

                if (i == key.length() - 1) {
                    nowMap.put("isEnd", "1");    //最后一个
                }
            }
        }
        return hashMap;
    }


    public ArrayList<EmojiBean> find(String toFindText) {
        ArrayList<EmojiBean> sensitiveWordList = new ArrayList<>();
        for (int i = 0; i < toFindText.length(); i++) {
            //判断是否包含敏感字符
            int length = find(toFindText, i, false);
            if (length > 0) {    //存在,加入list中
                String word = toFindText.substring(i, i + length);
                EmojiBean emojiBean = new EmojiBean();
                emojiBean.setEmoji(word);
                emojiBean.setStart(i);
                emojiBean.setEnd(i + word.length());
                sensitiveWordList.add(emojiBean);
                i = i + length - 1;    //减1的原因，是因为for会自增
            }
        }

        sensitiveWordList.sort(new Comparator<EmojiBean>() {
            @Override
            public int compare(EmojiBean o1, EmojiBean o2) {
                return o1.getStart() - o2.getStart();
            }
        });
        return sensitiveWordList;
    }

    public boolean isContainsEmoji(String txt) {
        for (int i = 0; i < txt.length(); i++) {
            //判断是否包含敏感字符
            int length = find(txt, i, true);
            if (length > 0) {
                return true;
            }
        }
        return false;
    }


    /**
     * 检查文字中是否包含敏感字符，检查规则如下：<br>
     *
     * @param txt
     * @param beginIndex
     * @param minMatch
     * @return 如果存在，则返回敏感词字符的长度，不存在返回0
     */
    @SuppressWarnings({"rawtypes"})
    private int find(String txt, int beginIndex, boolean minMatch) {
        boolean flag = false;    //敏感词结束标识位：用于敏感词只有1位的情况
        int matchFlag = 0;     //匹配标识数默认为0
        char word;
        Map nowMap = mMatchHashMap;
        for (int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word);     //获取指定key
            if (nowMap != null) {     //存在，则判断是否为最后一个
                matchFlag++;     //找到相应key，匹配标识+1
                if ("1".equals(nowMap.get("isEnd"))) {       //如果为最后一个匹配规则,结束循环，返回匹配标识数
                    flag = true;       //结束标志位为true
                    if (minMatch) {    //最小规则，直接返回,最大规则还需继续查找
                        break;
                    }
                }
            } else {     //不存在，直接返回
                break;
            }
        }
        /*“粉饰”匹配词库：“粉饰太平”竟然说是敏感词
         * “个人”匹配词库：“个人崇拜”竟然说是敏感词
         * if(matchFlag < 2 && !flag){
            matchFlag = 0;
        }*/
        if (!flag) {
            matchFlag = 0;
        }
        return matchFlag;
    }

}
