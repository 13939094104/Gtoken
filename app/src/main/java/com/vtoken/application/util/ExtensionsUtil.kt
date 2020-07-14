package com.vtoken.application.util

import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.vtoken.application.ApplicationLoader
import com.vtoken.application.R
import vdsMain.tool.DeviceUtil
import java.math.BigDecimal
import java.text.DecimalFormat

fun Double.toVdsDecimal():String{
    val df= DecimalFormat()
    df.maximumFractionDigits=8
    return df.format(this)
}

fun BigDecimal.toVdsString():String{
    if(this.compareTo(BigDecimal.ZERO)==0){
        return BigDecimal.ZERO.toPlainString()
    }else{
        return this.setScale(8).stripTrailingZeros().toPlainString()
    }
}



fun BottomNavigationBar.resetItemSize(space: Int, imgLen: Int, textSize: Int){
    val barClass = this.javaClass
    val fields = barClass.declaredFields
    val context=ApplicationLoader.applicationContext
    for (i in fields.indices) {
        val field = fields[i]
        field.isAccessible = true
        if (field.name.equals("mTabContainer")) {
            try {
                //反射得到 mTabContainer
                val mTabContainer = field.get(this) as LinearLayout
                for (j in 0 until mTabContainer.childCount) {
                    //获取到容器内的各个Tab
                    val view = mTabContainer.getChildAt(j)
                    view.post {
                        view.layoutParams= LinearLayout.LayoutParams(mTabContainer.width/mTabContainer.childCount,
                            ViewGroup.LayoutParams.MATCH_PARENT)
                    }
                    //获取到Tab内的各个显示控件
                    var params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DeviceUtil.dp2px(context,56f))
                    val container = view.findViewById(R.id.fixed_bottom_navigation_container) as FrameLayout
                    container.layoutParams = params
                    container.setPadding(
                        DeviceUtil.dp2px(context,0f),
                        DeviceUtil.dp2px(context,0f),
                        DeviceUtil.dp2px(context,0f),
                        DeviceUtil.dp2px(context,0f))

                    //获取到Tab内的文字控件
                    val labelView =
                        view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_title) as TextView
                    //计算文字的高度DP值并设置，setTextSize为设置文字正方形的对角线长度，所以：文字高度（总内容高度减去间距和图片高度）*根号2即为对角线长度，此处用DP值，设置该值即可。
                    labelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize.toFloat())
                    labelView.includeFontPadding = false
                    labelView.setPadding(0, 0, 0, DeviceUtil.dp2px(context,(20 - textSize - space / 2).toFloat()))
                    //获取到Tab内的图像控件
                    val iconView = view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_icon) as ImageView
                    //设置图片参数，其中，MethodUtils.dip2px()：换算dp值
                    params = FrameLayout.LayoutParams(
                        DeviceUtil.dp2px(context,imgLen.toFloat()),
                        DeviceUtil.dp2px(context,imgLen.toFloat()))
                    params.setMargins(0, 0, 0, space / 2)
                    params.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                    iconView.setLayoutParams(params)
                }
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

        }
    }
}


fun com.ydit.lottilewidget.BottomNavigationBar.resetItemSize(space: Int, imgLen: Int, textSize: Int){
    val barClass = this.javaClass
    val fields = barClass.declaredFields
    val context=ApplicationLoader.applicationContext
    for (i in fields.indices) {
        val field = fields[i]
        field.isAccessible = true
        if (field.name.equals("mTabContainer")) {
            try {
                //反射得到 mTabContainer
                val mTabContainer = field.get(this) as LinearLayout
                for (j in 0 until mTabContainer.childCount) {
                    //获取到容器内的各个Tab
                    val view = mTabContainer.getChildAt(j)
                    view.post {
                        view.layoutParams= LinearLayout.LayoutParams(mTabContainer.width/mTabContainer.childCount,
                            ViewGroup.LayoutParams.MATCH_PARENT)
                    }
                    //获取到Tab内的各个显示控件
                    var params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DeviceUtil.dp2px(context,56f))
                    val container = view.findViewById(R.id.fixed_bottom_navigation_container) as FrameLayout
                    container.layoutParams = params
                    container.setPadding(
                        DeviceUtil.dp2px(context,0f),
                        DeviceUtil.dp2px(context,0f),
                        DeviceUtil.dp2px(context,0f),
                        DeviceUtil.dp2px(context,0f))

                    //获取到Tab内的文字控件
                    val labelView =
                        view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_title) as TextView
                    //计算文字的高度DP值并设置，setTextSize为设置文字正方形的对角线长度，所以：文字高度（总内容高度减去间距和图片高度）*根号2即为对角线长度，此处用DP值，设置该值即可。
                    labelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize.toFloat())
                    labelView.includeFontPadding = false
                    labelView.setPadding(0, 0, 0, DeviceUtil.dp2px(context,(20 - textSize - space / 2).toFloat()))
                    //获取到Tab内的图像控件
                    val iconView = view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_icon) as ImageView
                    //设置图片参数，其中，MethodUtils.dip2px()：换算dp值
                    params = FrameLayout.LayoutParams(
                        DeviceUtil.dp2px(context,imgLen.toFloat()),
                        DeviceUtil.dp2px(context,imgLen.toFloat()))
                    params.setMargins(0, 0, 0, space / 2)
                    params.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                    iconView.setLayoutParams(params)
                }
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

        }
    }
}