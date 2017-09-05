# bloodcrownbitmapcachetest
使用png图片内存变化探讨

一直以来我总是知道 png 机器耗费内存,但是在我们大量使用 png 图片后,集合系统的 CG 垃圾回收机制,内存的具体变化又是怎么的呢,今天就来特意的玩一玩

android显示图片的基础是bitmap 位图显示机制，我们使用的静态 png 图片在显示时也是以 bitmap 来显示的，所以 android 中使用静态 png图片还是网络图片资源都是以 bitmap 核心来显示的，内存占用和变化页都是一样的，所以我使用静态 png 图片来简单的模拟一下

### 测试设计

我在 app 中放入5张 1080p 的 png 图片，还有一张系统的 icon，点击图片让其切换显示到下一张，点击的速度略快，以模拟我们平时大量加载网络端资源,然后结合一下手动 bitmap 资源回收看一下

代码：
```java

// 图片切换方法
public void nextImage(View view) {
        List<Integer> images = getImages();
        if (index == images.size() - 1) {
            index = 0;
        }
//        recycleBitmap(image);
        image.setImageResource(images.get(index));
        index++;
    }

// 手动位图资源回收
    private void recycleBitmap(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }
```

代码很简单，我们来看下效果图：

我们使用 app：
![ezgif.com-video-to-gif.gif](http://upload-images.jianshu.io/upload_images/1785445-339b17c7b81b5724.gif?imageMogr2/auto-orient/strip)

不手动回收bitmap 资源：
![no-recycle.png](http://upload-images.jianshu.io/upload_images/1785445-d1b52b11d85576a3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

手动回收bitmap 资源：
![recycle.png](http://upload-images.jianshu.io/upload_images/1785445-499fdc412f8272d3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


### 对比结果

没有对比就看不到差距，因为我用的都是1080p 大图，所以单张图占用内存颇大，效果比较明显

* 内存占用：
  * app 不加载图片时起始占用3.7M内存
  * 手动回收bitmap 资源时：内存占用峰值58M，最后不动时占用54M
  * 不收回 bitmap 资源时：内存占用峰值85M，最后不动时占用38M

* CG 波动：
  * 手动回收bitmap 资源时：CG 平滑很多，比较频繁的
  * 不收回 bitmap 资源时：CG 波动很尖锐，非常频繁

从上面的对比可以看到，我们平时多多回收 bitmap 位图资源效果还是很明显的，但是资源占用和 CG 还是很不理想的，所以有一下几个结论大家参考下：

* 结合图片加载库，首先把库的内存缓存设置小一点，其次在页面关闭时手动清理图片内存缓存库，最后在显示图片变换时多多手动清理 bitmap 位图资源
* 对于app 的静态 png图片，分辨率能小就小，尽可能的使用 SVG 矢量图代替 png 图片，SVG 矢量图我们可以设置成1dp*1dp 的，会节省很对内存资源。

ps： 恩，上面的我也是简单的跑了跑，能为高玩要是能有更好的资料屏留言发给我，谢啦。
