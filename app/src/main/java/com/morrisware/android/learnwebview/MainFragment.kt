package com.morrisware.android.learnwebview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.morrisware.android.learnwebview.R.id.*
import com.morrisware.android.learnwebview.databinding.MainFragmentBinding
import com.morrisware.android.learnwebview.event.RefreshItemEvent
import com.morrisware.android.learnwebview.event.RemoveItemEvent
import com.morrisware.android.learnwebview.event.ToggleViewPagerEvent
import io.reactivex.functions.Consumer

/**
 * Created by MorrisWare on 2018/10/25.
 * Email: MorrisWare01@gmail.com
 */
class MainFragment : Fragment() {

    lateinit var binding: MainFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.main_fragment,
            container,
            false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initWebView()
        viewLifecycleOwner.lifecycle.registerEvent(RefreshItemEvent::class.java, Consumer {
            binding.btnWindow.text = if (it.isZoom) "关闭" else "窗口"
        })

        binding.btnWindow.setOnClickListener { clickWindow() }

        binding.webView.loadUrl("http://www.baidu.com")
    }

    private fun clickWindow() {
        if (binding.btnWindow.text == "关闭") {
            binding.root.apply {
                val animator = ObjectAnimator.ofFloat(this, "translationY", 0f, -context.getScreenHeight().toFloat())
                animator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        RxBus.getInstance().post(RemoveItemEvent(0))
                    }
                })
                animator.duration = 300
                animator.start()
            }
        } else {
            RxBus.getInstance().post(ToggleViewPagerEvent(true))
        }
    }

    override fun onResume() {
        super.onResume()
        binding.webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.webView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.webView?.apply {
            (parent as ViewGroup).removeView(this)
            destroy()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        binding.webView.settings.apply {
            javaScriptEnabled = true
            // 设置自适应屏幕，两者合用
            useWideViewPort = true
            setSupportZoom(false)

        }
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }
        }
        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                binding.tvTitle.text = title
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                binding.progressBar.apply {
                    if (newProgress == 100) {
                        visibility = View.GONE
                    } else {
                        progress = newProgress
                        visibility = View.VISIBLE
                    }
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