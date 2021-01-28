

本文主要分享下直播中的弹幕、键盘还有带货卡片的交互效果的实现方式

<img src="https://github.com/dawangzai/LiveBullet/blob/master/gif1.gif" width="30%" height="30%"> <img src="https://github.com/dawangzai/LiveBullet/blob/master/gif2.gif" width="30%" height="30%"> <img src="https://github.com/dawangzai/LiveBullet/blob/master/gif3.gif" width="30%" height="30%">


## 实现思路
通过监听键盘的状态以及商品卡片的状态对弹幕区域做 `translationY` 动画。实现思路就很简单，下面有一些难点
- 监听键盘状态的实现方法
- 键盘的高度怎么获取



## 代码地址
具体实现的时候还要有很多细节需要处理，下面是代码地址 

[代码地址]("代码地址")
