

本文主要分享下直播中的弹幕、键盘还有带货卡片的交互效果的实现方式

<img src="https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/3532509c9b7a488abaf6f2faf623983c~tplv-k3u1fbpfcp-watermark.image" width="30%" height="30%"> <img src="https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/254946a7d2884eebabfee82c37b72bda~tplv-k3u1fbpfcp-watermark.image" width="30%" height="30%"> <img src="https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/339ee6a72c4045f0b22fcdea6b04ec10~tplv-k3u1fbpfcp-watermark.image" width="30%" height="30%">


## 实现思路
通过监听键盘的状态以及商品卡片的状态对弹幕区域做 `translationY` 动画。实现思路就很简单，下面有一些难点
- 监听键盘状态的实现方法
- 键盘的高度怎么获取



## 代码地址
具体实现的时候还要有很多细节需要处理，下面是代码地址 

[代码地址]("代码地址")
