/**
 * @author Created by qiyei2015 on 2020/4/20.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: MockData
 */
package com.qiyei.audio.model

class MockData {


    companion object {

        val queues:MutableList<AudioBean> get() {
            val list = mutableListOf<AudioBean>()
            list.add(
                AudioBean(
                    "以你的名字", "七里香", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典", "周杰伦",
                    "http://sp-sycdn.kuwo.cn/resource/n2/85/58/433900159.mp3","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698076304&di=e6e99aa943b72ef57b97f0be3e0d2446&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fblog%2F201401%2F04%2F20140104170315_XdG38.jpeg",
                    "2:05"
                )
            )
            list.add(
                AudioBean(
                     "勇气",
                    "勇气", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典", "梁静茹",
                    "http://sq-sycdn.kuwo.cn/resource/n1/98/51/3777061809.mp3","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698193627&di=711751f16fefddbf4cbf71da7d8e6d66&imgtype=jpg&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D213168965%2C1040740194%26fm%3D214%26gp%3D0.jpg",
                    "4:40"
                )
            )
            list.add(
                AudioBean(
                     "灿烂如你",
                    "春天里", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典", "汪峰",
                    "http://sp-sycdn.kuwo.cn/resource/n2/52/80/2933081485.mp3","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698239736&di=3433a1d95c589e31a36dd7b4c176d13a&imgtype=0&src=http%3A%2F%2Fpic.zdface.com%2Fupload%2F201051814737725.jpg",
                    "3:20"
                )
            )
            list.add(
                AudioBean(
                     "小情歌",
                    "小幸运", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典", "五月天",
                    "http://sr-sycdn.kuwo.cn/resource/n2/33/25/2629654819.mp3","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698289780&di=5146d48002250bf38acfb4c9b4bb6e4e&imgtype=0&src=http%3A%2F%2Fpic.baike.soso.com%2Fp%2F20131220%2Fbki-20131220170401-1254350944.jpg",
                    "2:45"
                )
            )
            return list
        }
    }

}