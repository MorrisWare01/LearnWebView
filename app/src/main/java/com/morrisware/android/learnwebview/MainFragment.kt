package com.morrisware.android.learnwebview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.morrisware.android.learnwebview.event.ToggleViewPager
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.main_fragment.*

/**
 * Created by MorrisWare on 2018/10/25.
 * Email: MorrisWare01@gmail.com
 */
class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initWebView()

        viewLifecycleOwner.lifecycle.registerEvent(ToggleViewPager::class.java, Consumer {
            view?.apply {
                scaleX
            }
        })

        btnWindow.setOnClickListener { clickWindow() }
        webView.loadUrl("http://www.baidu.com")
    }

    private fun clickWindow() {
        if (btnWindow.text == "close") {
            RxBus.getInstance().post(ToggleViewPager(false))
        } else {
            RxBus.getInstance().post(ToggleViewPager(true))

            val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.5f)
            val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.5f)
            val animatorSet = AnimatorSet()
            animatorSet.playTogether(scaleX, scaleY)
            animatorSet.duration = 300
            animatorSet.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    btnWindow.text = "close"
                }
            })
            animatorSet.start()
        }
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        webView?.apply {
            (parent as ViewGroup).removeView(this)
            destroy()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        webView.settings.apply {
            javaScriptEnabled = true
        }
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return true
            }
        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                tvTitle.text = title
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    progressBar.visibility = View.GONE
                } else {
                    progressBar.progress = newProgress
                    progressBar.visibility = View.VISIBLE
                }
            }

            override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
                result.confirm()
                return true
            }

            override fun onJsConfirm(view: WebView, url: String, message: String, result: JsResult): Boolean {
                //        LogUtil.e("onJsConfirm:" + url + ":" + message);
                AlertDialog.Builder(view.context)
                    .setMessage(message)
                    .setPositiveButton("确定", { dialog, which -> result.confirm() })
                    .setNegativeButton("取消", { dialog, which -> result.cancel() })
                    .setCancelable(false)
                    .show()
                return true
            }

            override fun onJsPrompt(view: WebView, url: String, message: String, defaultValue: String, result: JsPromptResult): Boolean {
                //        LogUtil.e("onJsPrompt:" + url + ":" + message + ":" + defaultValue);
                val editText = EditText(view.context)
                editText.setText(defaultValue)
                editText.setSelection(editText.length())
                AlertDialog.Builder(view.context)
                    .setTitle(message)
                    .setView(editText)
                    .setPositiveButton("确定", { dialog, which -> result.confirm(editText.text.toString()) })
                    .setNegativeButton("取消", { dialog, which -> result.cancel() })
                    .setCancelable(false)
                    .show()
                return true
            }
        }


    }

}