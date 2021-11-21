package io.flutter.plugins.camera.types;

import android.util.Size;

public class CameraOutputConfig {
    private final Size captureSize;
    private final Size previewSize;
    private final int format;

    public CameraOutputConfig(Size captureSize, Size previewSize, int format) {
        this.captureSize = captureSize;
        this.previewSize = previewSize;
        this.format = format;
    }

    public Size getCaptureSize() {
        return captureSize;
    }

    public Size getPreviewSize() {
        return previewSize;
    }

    public int getFormat() {
        return format;
    }
}