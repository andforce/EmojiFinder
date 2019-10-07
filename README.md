# EmojiFinder

无论你是前端、后端还是客户端开发，文字处理部分几乎都会遇到，而`emoji`又是一个很让人头疼的问题。

原因在于：

1.有部分网站的后端数据库无法存储emoji表情

2.有部分前端浏览器啥的无法正常显示emoji表情

3.部分客户端逻辑需要特殊处理emoji

因此针对上述的问题，无论你采取什么方式处理，首先找到emoji是第一步，否则根本无法进行下一步的动作。


## 先认识Emoji

总体来看，emoji按照其长度分类，总共分了11类（1、2、3、4、5、6、7、8、10、11、14），这也是为什么emoji处理困难的原因。

下面这些，我每个长度选择了一个展示，前面是对应的unicode
```
\u2764		[❤]		1
\u23ED\uFE0F		[⏭️]		2
\uD83D\uDEF3\uFE0F		[🛳️]		3
\uD83E\uDDDF\u200D\u2642		[🧟‍♂]		4
\u26F9\uD83C\uDFFB\u200D\u2642		[⛹🏻‍♂]		5
\uD83E\uDDDD\uD83C\uDFFF\u200D\u2642		[🧝🏿‍♂]		6
\uD83E\uDDDD\uD83C\uDFFF\u200D\u2642\uFE0F		[🧝🏿‍♂️]		7
\uD83D\uDC68\u200D\uD83D\uDC67\u200D\uD83D\uDC66		[👨‍👧‍👦]		8
\uD83D\uDC69\u200D\u2764\u200D\uD83D\uDC8B\u200D\uD83D\uDC68		[👩‍❤‍💋‍👨]		10
\uD83D\uDC68\u200D\uD83D\uDC68\u200D\uD83D\uDC66\u200D\uD83D\uDC66		[👨‍👨‍👦‍👦]		11
\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F		[🏴󠁧󠁢󠁥󠁮󠁧󠁿]		14

```

## 问题分析

查找过滤字符串，自然想到的是正则表达式，

首先是查找资料，根据我的经验，国内的大部分博客基本都是翻译或者复制粘贴的。

所以，我查找了unicode的官方网站，http://www.unicode.org/reports/tr51/。

在正则部分 http://www.unicode.org/reports/tr51/#EBNF_and_Regex
 
找到了相关内容：

```
  \p{RI} \p{RI}
| \p{Emoji} 
  ( \p{EMod} 
    | \x{FE0F} \x{20E3}? 
    | [\x{E0020}-\x{E007E}]+ \x{E007F} )?
  (\x{200D} \p{Emoji} 
    ( \p{EMod}
      | \x{FE0F} \x{20E3}? )?)+
```

然而，java中根本没有`p{Emoji}`这个表达式。

于是正则这个方案暂时放弃了。

## 通过DFA算法查找
由于emoji目前是有限个数，可以通过DFA算法过滤。

## Usage：
```java
//Test String from                                                                                                              
//http://unicode.org/Public/emoji/latest/emoji-test.txt                                                                         
                                                                                                                                
String toFind = "# subgroup: person-activity" +                                                                                 
        "1F486                                      ; fully-qualified     # 💆 person getting massage" +                         
        "1F486 1F3FB                                ; fully-qualified     # 💆🏻 person getting massage: light skin tone" +       
        "1F486 1F3FC                                ; fully-qualified     # 💆🏼 person getting massage: medium-light skin tone" +
        "1F486 1F3FD                                ; fully-qualified     # 💆🏽 person getting massage: medium skin tone" +      
        "1F486 1F3FE                                ; fully-qualified     # 💆🏾 person getting massage: medium-dark skin tone" + 
        "1F486 1F3FF                                ; fully-qualified     # 💆🏿 person getting massage: dark skin tone " +       
        "1F486 200D 2642 FE0F                       ; fully-qualified     # 💆‍♂️ man getting massage" +                         
        "1F486 200D 2642                            ; minimally-qualified # 💆‍♂ man getting massage " +                         
        "1F486 1F3FB 200D 2642 FE0F                 ; fully-qualified     # 💆🏻‍♂️ man getting massage: light skin tone" +       
        "1F486 1F3FB 200D 2642                      ; minimally-qualified # 💆🏻‍♂ man getting massage: light skin tone " +       
        "1F486 1F3FC 200D 2642 FE0F                 ; fully-qualified     # 💆🏼‍♂️ man getting massage: medium-light skin tone" +
        "1F486 1F3FC 200D 2642                      ; minimally-qualified # 💆🏼‍♂ man getting massage: medium-light skin tone" + 
        "1F486 1F3FD 200D 2642 FE0F                 ; fully-qualified     # 💆🏽‍♂️ man getting massage: medium skin tone" +      
        "1F486 1F3FD 200D 2642                      ; minimally-qualified # 💆🏽‍♂ man getting massage: medium skin tone" +       
        "1F486 1F3FE 200D 2642 FE0F                 ; fully-qualified     # 💆🏾‍♂️ man getting massage: medium-dark skin tone" + 
        "1F486 1F3FE 200D 2642                      ; minimally-qualified # 💆🏾‍♂ man getting massage: medium-dark skin tone " + 
        "1F486 1F3FF 200D 2642 FE0F                 ; fully-qualified     # 💆🏿‍♂️ man getting massage: dark skin tone " +       
        "1F486 1F3FF 200D 2642                      ; minimally-qualified # 💆🏿‍♂ man getting massage: dark skin tone";          
                                                                                                                                
long start = System.currentTimeMillis();                                                                                        
                                                                                                                                
ArrayList<EmojiBean> findResult = EmojiFinder.getInstance().find(toFind);                                                       
                                                                                                                                
System.out.println("Use Time: " + (System.currentTimeMillis() - start) + ", Find Count : " + findResult.size());                
System.out.println("Find Result : " + findResult);                                                                                                          
```

```
http://www.unicode.org/Public/emoji/12.0/emoji-test.txt
# Status Counts
# fully-qualified : 3010
# minimally-qualified : 571
# unqualified : 246
# component : 9
Emoji总共3836个
```



