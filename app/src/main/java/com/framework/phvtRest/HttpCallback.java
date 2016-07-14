package com.framework.phvtRest;

/**
 * Base callback for AsyncTask
 *
 * @param <Params>   Parameters
 * @param <Progress> Progress values
 * @param <Result>   Result post to UI Thread
 * @author PhatVan ヴァン  タン　ファット
 */

public abstract class HttpCallback<Params, Progress, Result> {

    //-------------------------------------------------------------------------------------------------------------------
    public void doPreExcute() {
    }

    //-------------------------------------------------------------------------------------------------------------------
    public void doPostExecute(Result result) {
    }

    //-------------------------------------------------------------------------------------------------------------------
    public Result doInBackground(Params... params) {
        return null;
    }

    //-------------------------------------------------------------------------------------------------------------------
    public void doProgressUpdate(Progress... values) {
    }

    //-------------------------------------------------------------------------------------------------------------------
    public void doCancelled() {
    }

    //-------------------------------------------------------------------------------------------------------------------
    public void doCancelled(Result result) {
    }

}