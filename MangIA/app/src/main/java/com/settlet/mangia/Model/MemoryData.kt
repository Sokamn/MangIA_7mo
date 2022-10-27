package com.settlet.mangia.Model

import android.content.Context
import java.io.*
import java.lang.StringBuilder

class MemoryData {
    companion object {
        fun saveData(data:String, context: Context) {
            try {
                val fos: FileOutputStream = context.openFileOutput("datata.txt",Context.MODE_PRIVATE)
                fos.write(data.toByteArray())
                fos.close()
            }
            catch (e: IOException){
                e.printStackTrace()
            }
        }
        fun saveLastMsgTS(data:String, chatID: String, context: Context) {
            try {
                val fos: FileOutputStream = context.openFileOutput("$chatID.txt",Context.MODE_PRIVATE)
                fos.write(data.toByteArray())
                fos.close()
            }
            catch (e: IOException){
                e.printStackTrace()
            }
        }
        fun getData(context: Context):String {
            var data = ""
            try {
                val fis: FileInputStream = context.openFileInput("datata.txt")
                val isr = InputStreamReader(fis)
                val bReader = BufferedReader(isr)
                val sb = StringBuilder()
                var line:String? = null
                while(line!=null){
                    line = bReader.readLine()
                    sb.append(line)
                }
                data = sb.toString()
            }
            catch (e: IOException){
                e.printStackTrace()
            }
            return data
        }
        fun getLastMsgTS(context: Context, chatID: String):String {
            var data = ""
            try {
                val fis: FileInputStream = context.openFileInput("$chatID.txt")
                val isr = InputStreamReader(fis)
                val bReader = BufferedReader(isr)
                val sb = StringBuilder()
                var line:String? = null
                while(line!=null){
                    line = bReader.readLine()
                    sb.append(line)
                }
                data = sb.toString()
            }
            catch (e: IOException){
                e.printStackTrace()
            }
            return data
        }
    }
}