package io.flutter.plugins.cameraexample;

import android.os.Bundle;
import io.flutter.plugins.camera.CameraPlugin;
import io.flutter.plugins.pathprovider.PathProviderPlugin;
import io.flutter.plugins.videoplayer.VideoPlayerPlugin;

@SuppressWarnings("deprecation")
public class EmbeddingV1Activity extends io.flutter.app.FlutterActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    CameraPlugin.registerWith(registrarFor("io.flutter.plugins.camera.CameraPlugin"));
    PathProviderPlugin.registerWith(
        registrarFor("io.flutter.plugins.pathprovider.PathProviderPlugin"));
    VideoPlayerPlugin.registerWith(
        registrarFor("io.flutter.plugins.videoplayer.VideoPlayerPlugin"));
  }
}
