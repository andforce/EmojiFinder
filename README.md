# EmojiFinder

## 为什么？
无论你是前端、后端还是客户端开发，文字处理部分几乎都会遇到，而`emoji`又是一个很让人头疼的问题。

原因在于：

1.有部分网站的后端数据库无法存储emoji表情

2.有部分前端浏览器啥的无法正常显示emoji表情

3.部分客户端逻辑需要特殊处理emoji

因此针对上述的问题，无论你采取什么方式处理，首先找到emoji是第一步，否则根本无法进行下一步的动作。

要查找过滤首先想到的肯定是利用正则表达式，然后你会去网上查找相关正则表达式，可问题来了，我查找了一些根本不全面，覆盖范围不广泛。

## 怎么做？

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

好在，unicode网站上，对emoji的就诶少很全，他们甚至整理了所有的emoji的列表：http://unicode.org/Public/emoji/latest/emoji-test.txt

## 分析

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

然后开始尝试写正则表达了

由于emoji的情况非常多，因此不可能一条就覆盖整个情况。

长度是7、8、10、11的emoji数量比较少，所以直接使用了String.indexOf()直接查找，因此正则表达式只要关系剩下的情况就好了。

```
    // 规则是先查找较长的emoji，原因是长的emoji可能包含短的emoji
    String[] regexArr = {
            "[\\uD83C\\uDFC3-\\uD83E\\uDDDD][\\uD83C\\uDFFB-\\uD83C\\uDFFF|\\uFE0F]\\u200D[\\u2640-\\u2708|\\uD83C\\uDF3E-\\uD83E\\uDDB3]\\uFE0F?",
            "\\u26F9?[\\uD83C\\uDFC3-\\uD83E\\uDDDD][\\uD83C\\uDFFB-\\uD83C\\uDFFF]?\\uFE0F?\\u200D[\\u2640-\\u2708|\\uD83C\\uDF08-\\uD83D\\uDDE8]\\uFE0F?",
            "[\\uD83C\\uDFF3-\\uD83D\\uDC69|\\u26F9]\\uFE0F?\\u200D[\\uD83C\\uDF08-\\uD83E\\uDDB3|\\u2620-\\u2708]\\uFE0F?",
            "[\\uD83C\\uDFC3-\\uD83E\\uDDDF]\\uFE0F?\\u200D[\\u2640-\\u2642]\\uFE0F?",
            "[\\uD83C\\uDF85-\\uD83E\\uDDDF][\\uD83C\\uDFFB-\\uD83C\\uDFFF|\\u200D[\\u2620-\\u2642]]",
            "[\\uD83C\\uDDE6-\\uD83C\\uDDFF][\\uD83C\\uDDE6-\\uD83C\\uDDFF]",
            "[\\u261D-\\u270D]?[\\uD83C\\uDC04-\\uD83E\\uDEF9]\\uFE0F?",
            "[\\u0023-\\u0039]\\uFE0F?\\u20E3|\\u00A9\\uFE0F?|\\u00AE\\uFE0F?",
            "[\\u203C-\\u3299]\\uFE0F?"
    };
```

## 结果
把上面的方案，封装到了`EmojiFinder.java`中，直接调用`find()`方法，即可。

经过测试，可以找出http://unicode.org/Public/emoji/latest/emoji-test.txt中列出的3570个emoji。

并且时间可以控制在100毫秒以内，正则部分也许还有优化的空间。