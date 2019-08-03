package com.example.autoslideshowapp

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_CODE = 100

    private var mTimer: Timer? = null

    // タイマー用の時間のための変数
    private var mTimerSec = 0.0

    private var mHandler = Handler()

    var cursor: Cursor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Android 6.0以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                getContentsInfo()
            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_CODE)
            }
            // Android 5系以下の場合
        } else {
            getContentsInfo()
        }

            go_button.setOnClickListener {

                    if (cursor!!.moveToNext()) {
                        // indexからIDを取得し、そのIDから画像のURIを取得する
                        val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                        val id = cursor!!.getLong(fieldIndex)
                        val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                        imageView.setImageURI(imageUri)
                    } else {
                        cursor!!.moveToFirst()
                        // indexからIDを取得し、そのIDから画像のURIを取得する
                        val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                        val id = cursor!!.getLong(fieldIndex)
                        val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                        imageView.setImageURI(imageUri)
                    }
            }

            back_button.setOnClickListener {

                    if (cursor!!.moveToPrevious()) {
                        // indexからIDを取得し、そのIDから画像のURIを取得する
                        val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                        val id = cursor!!.getLong(fieldIndex)
                        val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                        imageView.setImageURI(imageUri)
                    } else {
                        cursor!!.moveToLast()
                        // indexからIDを取得し、そのIDから画像のURIを取得する
                        val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                        val id = cursor!!.getLong(fieldIndex)
                        val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                        imageView.setImageURI(imageUri)
                    }
            }

            auto_button.setOnClickListener {

                if (mTimer == null) {
                    mTimer = Timer()
                    mTimer!!.schedule(object : TimerTask() {
                        override fun run() {
                            mTimerSec += 0.1
                            mHandler.post {
                                if (cursor!!.moveToNext()) {
                                    // indexからIDを取得し、そのIDから画像のURIを取得する
                                    val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                                    val id = cursor!!.getLong(fieldIndex)
                                    val imageUri =
                                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                                    imageView.setImageURI(imageUri)
                                } else {
                                    cursor!!.moveToFirst()
                                    // indexからIDを取得し、そのIDから画像のURIを取得する
                                    val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                                    val id = cursor!!.getLong(fieldIndex)
                                    val imageUri =
                                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                                    imageView.setImageURI(imageUri)
                                }
                            }
                        }
                    }, 2000, 2000)

                    go_button.setEnabled(false)
                    back_button.setEnabled(false)

                    auto_button.setText("停止")
                } else {
                    mTimer!!.cancel()
                    mTimer = null

                    go_button.setEnabled(true)
                    back_button.setEnabled(true)

                    auto_button.setText("再生")
                }
            }
        }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
            when (requestCode) {
            PERMISSIONS_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo()
                }
        }
    }

    private fun getContentsInfo() {
        // 画像の情報を取得する
        val resolver = contentResolver
        cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
            null, // 項目(null = 全項目)
            null, // フィルタ条件(null = フィルタなし)
            null, // フィルタ用パラメータ
            null // ソート (null ソートなし)
        )

        if (cursor != null) {
            if (cursor!!.moveToFirst()) {
                    // indexからIDを取得し、そのIDから画像のURIを取得する
                    val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = cursor!!.getLong(fieldIndex)
                    val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    imageView.setImageURI(imageUri)
            }

        }
    }
}