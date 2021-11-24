// Copyright 2019 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.camera;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.camera.types.CaptureTimeoutsWrapper;

class PictureCaptureRequest {

    private static final String TAG = "PictureCaptureRequest";

    public CaptureTimeoutsWrapper getCaptureTimeouts() {
        return captureTimeouts;
    }

    enum State {
        idle,
        focusing,
        preCapture,
        waitingPreCaptureReady,
        capturing,
        finished,
        error,
    }

    private final Runnable timeoutCallback =
            new Runnable() {
                @Override
                public void run() {
                    error("captureTimeout", "Picture capture request timed out", state.toString());
                }
            };

    private final MethodChannel.Result result;
    private final TimeoutHandler timeoutHandler;
    private final CaptureTimeoutsWrapper captureTimeouts;
    private State state;


    public PictureCaptureRequest(MethodChannel.Result result,
                                 CaptureTimeoutsWrapper captureTimeouts) {
        this(result, new TimeoutHandler(), captureTimeouts);
    }

    public PictureCaptureRequest(MethodChannel.Result result,
                                 TimeoutHandler timeoutHandler,
                                 CaptureTimeoutsWrapper captureTimeouts) {
        this.result = result;
        this.state = State.idle;
        this.timeoutHandler = timeoutHandler;
        this.captureTimeouts = captureTimeouts;
    }

    public void setState(State state) {
        Log.d(TAG, "setState:" + state.name());
        if (isFinished()) throw new IllegalStateException("Request has already been finished");
        this.state = state;
        if (state != State.idle && state != State.finished && state != State.error) {
            this.timeoutHandler.resetTimeout(timeoutCallback);
        } else {
            this.timeoutHandler.clearTimeout(timeoutCallback);
        }
    }

    public State getState() {
        return state;
    }

    public boolean isFinished() {
        return state == State.finished || state == State.error;
    }

    public void finish(String absolutePath) {
        if (isFinished()) throw new IllegalStateException("Request has already been finished");
        this.timeoutHandler.clearTimeout(timeoutCallback);
        result.success(absolutePath);
        state = State.finished;
        Log.d(TAG, "setState:" + state.name());
    }

    public void error(
            String errorCode, @Nullable String errorMessage, @Nullable Object errorDetails) {
        if (isFinished()) throw new IllegalStateException("Request has already been finished");
        this.timeoutHandler.clearTimeout(timeoutCallback);
        result.error(errorCode, errorMessage, errorDetails);
        state = State.error;
        Log.d(TAG, "setState:" + state.name());
    }

    static class TimeoutHandler {
        private static final int REQUEST_TIMEOUT = 5000;
        private final Handler handler;

        TimeoutHandler() {
            this.handler = new Handler(Looper.getMainLooper());
        }

        public void resetTimeout(Runnable runnable) {
            clearTimeout(runnable);
            handler.postDelayed(runnable, REQUEST_TIMEOUT);
        }

        public void clearTimeout(Runnable runnable) {
            handler.removeCallbacks(runnable);
        }
    }
}
