package com.backbase.android.flow.uploadfiles

/**
 * Created by Backbase R&D B.V. on 2020-05-29.
 *
 * The exit points for the Upload files Journey.
 */
interface UploadFilesRouter {

    /**
     * Close Upload files Journey
     */
    fun onUploadFilesFinished()
}