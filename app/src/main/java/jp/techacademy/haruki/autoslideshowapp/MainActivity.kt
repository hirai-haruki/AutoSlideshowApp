package jp.techacademy.haruki.autoslideshowapp

import android.content.ContentUris
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import jp.techacademy.haruki.autoslideshowapp.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val PERMISSIONS_REQUEST_CODE = 100
    private var cursor: Cursor? = null

    private var timer: Timer? = null
    private var seconds = 0.0
    private var handler = Handler(Looper.getMainLooper())

    private val readImagesPermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) android.Manifest.permission.READ_MEDIA_IMAGES
        else android.Manifest.permission.READ_EXTERNAL_STORAGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // パーミッションの許可状態を確認する
        if (checkSelfPermission(readImagesPermission) == PackageManager.PERMISSION_GRANTED) {
            // 許可されている
            getContentsInfo()
        } else {
            // 許可されていないので許可ダイアログを表示する
            requestPermissions(
                arrayOf(readImagesPermission),
                PERMISSIONS_REQUEST_CODE
            )
        }

        binding.moveButton.setOnClickListener {
            val resolver = contentResolver
            val cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null,
            )

            if (timer == null) {
                timer = Timer()
                timer!!.schedule(object : TimerTask() {
                    override fun run() {
                        seconds += 2
                        handler.post {
                            if (cursor!!.moveToNext()) {


                                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                                val id = cursor.getLong(fieldIndex)
                                val imageUri =
                                    ContentUris.withAppendedId(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        id
                                    )

                                binding.imageView.setImageURI(imageUri)
                            }
                        }
                    }
                }, 2000, 2000) // 最初に始動させるまで2000ミリ秒、ループの間隔を2000ミリ秒 に設定
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
        val cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
            null, // 項目（null = 全項目）
            null, // フィルタ条件（null = フィルタなし）
            null, // フィルタ用パラメータ
            null // ソート (nullソートなし）
        )

        if (cursor!!.moveToFirst()) {
            // indexからIDを取得し、そのIDから画像のURIを取得する
            val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val id = cursor.getLong(fieldIndex)
            val imageUri =
                ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

            binding.imageView.setImageURI(imageUri)




            binding.nextButton.setOnClickListener {
                if (cursor!!.moveToNext()) {
                    val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = cursor.getLong(fieldIndex)
                    val imageUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    binding.imageView.setImageURI(imageUri)
                }
            }
            binding.backButton.setOnClickListener {
                if (cursor!!.moveToPrevious()) {
                    val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = cursor.getLong(fieldIndex)
                    val imageUri =
                        ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
                        )

                    binding.imageView.setImageURI(imageUri)
                }
            }
        }
    }
}













// binding.nextButton.setOnClickListener {
//            if (cursor!!.moveToNext()) {
//                    // indexからIDを取得し、そのIDから画像のURIを取得する
//                    val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
//                    val id = cursor!!.getLong(fieldIndex)
//                    val imageUri =
//                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
//                    binding.imageView.setImageURI(imageUri)
//
//
//                    Log.d("ANDROID", "URI : $imageUri")




//          if(cursor!!.moveToNext()){
//                do {
//                    // indexからIDを取得し、そのIDから画像のURIを取得する
//                    val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
//                    val id = cursor.getLong(fieldIndex)
//                    val imageUri =
//                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
//                    binding.imageView.setImageURI(imageUri)
//
//                } while (cursor.moveToLast())
//            }
//            if(cursor!!.moveToNext()){
//                do {
//                    // indexからIDを取得し、そのIDから画像のURIを取得する
//                    val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
//                    val id = cursor.getLong(fieldIndex)
//                    val imageUri =
//                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
//                    binding.imageView.setImageURI(imageUri)











// binding.backButton.setOnClickListener {
//            binding.editText.setText(preference.getString("TEXT", "まだ保存されていません"))
//        }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        val view = binding.root
//        setContentView(view)
//
//
//        binding.nextButton.setOnClickListener {
//
//        }
//
//
//        binding.moveButton.setOnClickListener {
//
//            val resolver = contentResolver
//            val cursor = resolver.query(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                null,
//                null,
//                null,
//                null,
//                )
//            if (timer == null) {
//                timer = Timer()
//                timer!!.schedule(object : TimerTask() {
//                    override fun run() {
//                        seconds += 2
//                        handler.post {
//                            if (cursor!!.moveToFirst()) {
//
//                                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
//                                val id = cursor.getLong(fieldIndex)
//                                val imageUri =
//                                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
//
//                                binding.imageView.setImageURI(imageUri)
//                            }
//                            cursor.close()
//
//
//                        }
//                        }
//                }, 2000, 2000) // 最初に始動させるまで2000ミリ秒、ループの間隔を2000ミリ秒 に設定
//            }
//        }
//        }
//
//    }