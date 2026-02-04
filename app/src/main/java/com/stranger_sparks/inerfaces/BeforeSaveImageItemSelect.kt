package com.stranger_sparks.inerfaces

import android.net.Uri
import java.io.File

interface BeforeSaveImageItemSelect {
    fun singleItemSelect(deleteUri: Uri, deleteUriPosition: Int, deleteFile: File, deleteFilePosition: Int,)
}