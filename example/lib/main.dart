import 'package:flutter/material.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:sceneview_flutter/sceneview_flutter.dart';
import 'package:sceneview_flutter/sceneview_node.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    checkCameraPermission();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Scene view example app'),
        ),
        body: Stack(
          children: [
            SceneView(
              onViewCreated: (controller) {
                print('flutter: onViewCreated');
                controller.addNode(SceneViewNode(
                  fileLocation: 'assets/models/MaterialSuite.glb',
                  position: KotlinFloat3(z: -1.0),
                  rotation: KotlinFloat3(x: 15),
                ));
              },
            ),
          ],
        ),
      ),
    );
  }

  void checkCameraPermission() async {
    var status = await Permission.camera.status;
    if (!status.isGranted) {
      await Permission.camera.request();
    }
  }
}
