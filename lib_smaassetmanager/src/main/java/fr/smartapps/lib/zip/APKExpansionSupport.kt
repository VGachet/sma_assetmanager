package fr.smartapps.lib.zip;

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.IOException
import java.util.*

/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */   object APKExpansionSupport {
    // The shared path to all app expansion files
    private const val EXP_PATH = "/Android/obb/"
    fun getAPKExpansionFiles(ctx: Context, mainVersion: Int, patchVersion: Int): Array<String?> {
        val packageName = ctx.packageName
        val ret = Vector<String>()
        if (Environment.getExternalStorageState() ==
                Environment.MEDIA_MOUNTED) {
            // Build the full path to the app's expansion files
            val root = Environment.getExternalStorageDirectory()
            val expPath = File(root.toString() + EXP_PATH + packageName)

            // Check that expansion file path exists
            if (expPath.exists()) {
                if (mainVersion > 0) {
                    val strMainPath = expPath.toString() + File.separator + "main." + mainVersion + "." + packageName + ".obb"
                    val main = File(strMainPath)
                    if (main.isFile) {
                        ret.add(strMainPath)
                    }
                }
                if (patchVersion > 0) {
                    val strPatchPath = expPath.toString() + File.separator + "patch." + mainVersion + "." + packageName + ".obb"
                    val main = File(strPatchPath)
                    if (main.isFile) {
                        ret.add(strPatchPath)
                    }
                }
            }
        }
        val retArray = arrayOfNulls<String>(ret.size)
        ret.toArray(retArray)
        return retArray
    }

    @JvmStatic
    @Throws(IOException::class)
    fun getAPKExpansionZipFile(ctx: Context, mainVersion: Int, patchVersion: Int): ZipResourceFile? {
        val expansionFiles = getAPKExpansionFiles(ctx, mainVersion, patchVersion)
        var apkExpansionFile: ZipResourceFile? = null
        for (expansionFilePath in expansionFiles) {
            apkExpansionFile = ZipResourceFile(expansionFilePath, apkExpansionFile)
        }
        return apkExpansionFile
    }
}