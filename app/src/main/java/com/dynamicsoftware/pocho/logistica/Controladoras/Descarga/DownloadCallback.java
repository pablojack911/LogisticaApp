package com.dynamicsoftware.pocho.logistica.Controladoras.Descarga;

import android.net.NetworkInfo;

/**
 * Created by Pocho on 26/04/2017.
 */

public interface DownloadCallback<T>
{
    /**
     * Indicates that the callback handler needs to update its appearance or information based on
     * the result of the task. Expected to be called from the main thread.
     */
    void updateFromDownload(T result);

    /**
     * Get the device's active network status in the form of a NetworkInfo object.
     */
    NetworkInfo getActiveNetworkInfo();

    /**
     * Indicates that the download operation has finished. This method is called even if the
     * download hasn't completed successfully.
     */
    void finishDownloading();

    interface Progress
    {
        int ERROR = -1;
        int CONNECT_SUCCESS = 0;
        int GET_INPUT_STREAM_SUCCESS = 1;
        int PROCESS_INPUT_STREAM_IN_PROGRESS = 2;
        int STREAM_DOWNLOAD_COMPLETE = 4;
        int PROCESS_PARSE_STREAM_STARTED = 5;
        int PROCESS_PARSE_STREAM_IN_PROGRESS = 6;
        int PROCESS_PARSE_STREAM_COMPLETED = 11;
        int INICIANDO = 99;
        int PROCESS_INCORPORANDO_FACTURAS = 7;
        int GRABANDO_RUTA = 8;
        int GRABANDO_FACTURA = 9;
        int GRABANDO_ITEMS_FACTURA = 10;
    }
}
