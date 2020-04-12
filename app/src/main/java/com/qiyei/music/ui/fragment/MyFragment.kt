package com.qiyei.music.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.qiyei.music.R
import com.qiyei.network.api.HttpManager
import com.qiyei.network.api.HttpRequest
import com.qiyei.network.api.IHttpTransferListener
import com.qiyei.network.server.retrofit.IRetrofitService

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my, container, false)
    }

    override fun onResume() {
        super.onResume()

        HttpManager().execute(childFragmentManager, buildDownloadRequest(),
            object : IHttpTransferListener<String?> {
                override fun onProgress(currentLength: Long, totalLength: Long ) {
                    val progress = (currentLength * 1.0 / totalLength * 100).toInt()
//                    mDownloadProgressBar.setProgress(progress)
//                    mDownloadProgressTv.setText("$progress%")
                    Log.i(TAG, "progress:$progress currentLength :$currentLength totalLength:$totalLength")
                }

                override fun onSuccess(response: String?) {
                    Log.i(TAG, "response:$response")
                }

                override fun onFailure(exception: Exception?) {}
            })
    }


    companion object {
        val TAG = "MyFragment";

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    /**
     * 下载请求
     * @return
     */
    private fun buildDownloadRequest(): HttpRequest<Any> {
        val url1 = "https://timgsa.baidu.com/"
        val url2 = "timg?image&quality=80&size=b9999_10000&sec=1585679717920&di=dfbdbfeccf57617cc1dc07381e2d95ca&imgtype=0&src=http%3A%2F%2F00.minipic.eastday.com%2F20170122%2F20170122145324_c074bd4d20c537b795f6cc97f90d9e50_2.jpeg"
        return HttpRequest.Builder<String>()
            .download()
            .setBaseUrl(url1)
            .setPathUrl(url2)
            .setFilePath(context?.cacheDir?.absolutePath + "1.jpg")
            .setBody(null)
            .setApiClazz(IRetrofitService::class.java)
            .build()
    }
}
