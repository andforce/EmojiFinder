package com.andforce.utils;

import com.andforce.beans.EmojiBean;

import java.util.*;

public class SensitiveWordMatcher {
    @SuppressWarnings({"rawtypes", "unchecked"})
    private Map sensitiveWordMap;

    private Set<String> mAllEmojies = new HashSet<>();

    public SensitiveWordMatcher(Set<String> keyWordSet) {
        updateSensitiveWords(keyWordSet);
    }

    public void updateSensitiveWords(Set<String> keyWordSet) {
        Set<String> fixed = new HashSet<>(keyWordSet.size());
        for (String key : keyWordSet) {
            if (key.length() == 1) {
                mAllEmojies.add(key);
            } else {
                fixed.add(key);
            }
        }
        sensitiveWordMap = makeSensitiveWordToHashMap(fixed);
    }

    public static int serachWord(String str,String key) {
        //记录查找次数
        int count = 0;
        //记录每次查找的下标位置,初始化
        int index = -1;
        //定义循环,如果index的位置不是-1,就一值查找
        while ((index = str.indexOf(key, index)) != -1) {
            index = index + key.length();
        }
        return count;
    }

    /**
     * @param txt      文字
     * @param minMatch 匹配规则: 1：最小匹配规则，2：最大匹配规则
     * @return 查找到的敏感词
     */
    public ArrayList<EmojiBean> matches(String txt, boolean minMatch) {


        ArrayList<EmojiBean> len1 = new ArrayList<>();

        for (String key : mAllEmojies) {
            if (minMatch) {

                int index = txt.indexOf(key);
                if (index != -1) {
                    String word = txt.substring(index, index + key.length());
                    EmojiBean emojiBean = new EmojiBean();
                    emojiBean.setEmoji(word);
                    emojiBean.setStart(index);
                    emojiBean.setEnd(index + word.length());
                    len1.add(emojiBean);
                    return len1;
                }
            } else {
                int index = -1;
                int fromIndex = 0;
                while ((index = txt.indexOf(key, fromIndex)) != -1) {
                    fromIndex = index + key.length();
                    String word = txt.substring(index, index + key.length());
                    EmojiBean emojiBean = new EmojiBean();
                    emojiBean.setEmoji(word);
                    emojiBean.setStart(index);
                    emojiBean.setEnd(index + word.length());
                    len1.add(emojiBean);
                }
            }
        }

        Set<Integer> starts = new HashSet<>();

        ArrayList<EmojiBean> sensitiveWordList = new ArrayList<>();
        for (int i = 0; i < txt.length(); i++) {
            //判断是否包含敏感字符
            int length = find(txt, i, minMatch);
            if (minMatch && length > 0) {
                String word = txt.substring(i, i + length);
                EmojiBean emojiBean = new EmojiBean();
                emojiBean.setEmoji(word);
                emojiBean.setStart(i);
                emojiBean.setEnd(i + word.length());
                sensitiveWordList.add(emojiBean);
                starts.add(i);
                return sensitiveWordList;
            }
            if (length > 0) {    //存在,加入list中
                String word = txt.substring(i, i + length);
                EmojiBean emojiBean = new EmojiBean();
                emojiBean.setEmoji(word);
                emojiBean.setStart(i);
                emojiBean.setEnd(i + word.length());
                sensitiveWordList.add(emojiBean);
                starts.add(i);
                i = i + length - 1;    //减1的原因，是因为for会自增
            }
        }

        for (EmojiBean emojiBean : len1) {
            if (!starts.contains(emojiBean.getStart())){
                sensitiveWordList.add(emojiBean);
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
        Map nowMap = sensitiveWordMap;
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


//  读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：<br>
//  中 ={
//     isEnd = 0
//  国 =   {
//  isEnd = 1
//  人 =      {
//           isEnd = 0
//  民 =         {
//              isEnd = 1
//           }
//        }      男  =      {
//           isEnd = 0
//  人 =         {
//              isEnd = 1
//           }
//        }
//     }
//  }五 ={
//     isEnd = 0
//  星 =   {
//        isEnd = 0
//  红 =      {
//           isEnd = 0
//  旗 =         {
//              isEnd = 1
//           }
//        }
//     }
//  }
    @SuppressWarnings({"rawtypes", "unchecked"})
    private Map makeSensitiveWordToHashMap(Set<String> keyWordSet) {
        //初始化敏感词容器，减少扩容操作
        Map sensitiveWordMap = new HashMap(keyWordSet.size());
        String key;
        Map nowMap;
        Map<String, String> newWorMap;
        //迭代keyWordSet
        for (String aKeyWordSet : keyWordSet) {
            key = aKeyWordSet;    //关键字
            nowMap = sensitiveWordMap;
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
        return sensitiveWordMap;
    }

}
