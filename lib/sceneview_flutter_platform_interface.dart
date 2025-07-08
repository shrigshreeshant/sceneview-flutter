import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'package:sceneview_flutter/sceneview_node.dart';
import 'package:vector_math/vector_math_64.dart';

import 'sceneview_flutter_method_channel.dart';

abstract class SceneviewFlutterPlatform extends PlatformInterface {
  /// Constructs a SceneviewFlutterPlatform.
  SceneviewFlutterPlatform() : super(token: _token);

  static final Object _token = Object();

  static SceneviewFlutterPlatform _instance = MethodChannelSceneViewFlutter();

  /// The default instance of [SceneviewFlutterPlatform] to use.
  ///
  /// Defaults to [MethodChannelSceneViewFlutter].
  static SceneviewFlutterPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [SceneviewFlutterPlatform] when
  /// they register themselves.
  static set instance(SceneviewFlutterPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<void> init(int sceneId) {
    throw UnimplementedError('init() has not been implemented.');
  }

  void addNode(SceneViewNode node) {
    throw UnimplementedError('addNode() has not been implemented.');
  }

  hitTest(double x, double y, double screenWidth, double screenHeight) {
    throw UnimplementedError('hitTest() has not been implemented.');
  }

  Stream<bool> planeDetected() {
    throw UnimplementedError('planeDetected() has not been implemented.');
  }

  Future<bool> takePhoto() {
    throw UnimplementedError('takePhoto() has not been implemented.');
  }

  Future<void> update(
    List<String> nodeNames, {
    SceneViewNode? node,
    Vector3? translation,
    Vector3? startPoint,
    Vector3? endPoint,
  }) {
    throw UnimplementedError('update() has not been implemented.');
  }

  void dispose(int sceneId) {
    throw UnimplementedError('dispose() has not been implemented.');
  }
}
