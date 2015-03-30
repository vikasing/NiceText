# NiceText
NiceText removes HTML Clutter from a Web Page, it tries to find out the concentration of the text on a wep page and uses heuristics to determine the main block of the text.
#####Example Usage
NiceText interface is implemented by NTImpl, it has a method `extract` which takes a URL (`String` type) as parameter, here is a simple usase:

```java
NiceText niceText = new NTImpl();
String text = niceText.extract("http://www.scientificamerican.com/article/common-parasite-could-manipulate-our-behavior/");
System.out.println(text);
```
This is what I get:

<em>Already a subscriber or purchased this issue? Sign In. Imagine a world without fear. It might be empowering to go about your daily life uninhibited by everyday distresses. You could cross highways with confidence, take on all kinds of daredevilry and watch horror flicks without flinching. Yet consider the prospect a little more deeply, and the possibilities become darker, even deadly. Our fears, after all, can protect us. The basic aversion that a mouse has for a cat, for instance, keeps the rodent out of death's jaws. But unfortunately for mice everywhere, there is a second enemy with which to contend, one that may prevent them from experiencing that fear in the first place. A unicellular organism (a protozoan), Toxoplasma gondii, can override a rodent's most basic survival instincts. The result is a rodent that does not race away from a cat but is instead strangely attracted to it. Already a subscriber or purchased this issue? Sign In.</em>



