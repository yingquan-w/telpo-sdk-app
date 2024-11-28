import 'package:flutter/services.dart';

class NativeBridge {
  static const MethodChannel _channel = MethodChannel('com.movmint.channel');

  // Call the Android Java method and get a result
  static Future<String> callNativeMethod(String argument) async {
    try {
      final String result = await _channel.invokeMethod('methodName', {'arg': argument});
      return result;
    } on PlatformException catch (e) {
      return 'Error: ${e.message}';
    }
  }
}
