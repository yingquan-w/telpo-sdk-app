import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: NFCPage(),
    );
  }
}

class NFCPage extends StatelessWidget {
  final MethodChannel _methodChannel = const MethodChannel('com.movmint.channel');

  NFCPage({Key? key}) : super(key: key);

  Future<void> sendApduCommand() async {
    try {
      final String response = await _methodChannel.invokeMethod('sendApduCommand', {
      });
      print('Response from NFC: $response');
    } on PlatformException catch (e) {
      print('Error: ${e.message}');
    }
  }


  Future<void> checkNFC() async {
    try {
      await _methodChannel.invokeMethod('checkNFC', {});
      print('NFC opened');
    } on PlatformException catch (e) {
      print('Check NFCError: ${e.message}');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('NFC APDU Demo')),
      body: Column(
        children: [
          ElevatedButton(
          onPressed: sendApduCommand,
          child: const Text('Open NFC'),
          ),
          SizedBox(height: 20),
          ElevatedButton(
            onPressed: checkNFC,
            child: const Text('CheckNFC'),
          ),
          SizedBox(height: 20),
          ElevatedButton(
            onPressed: sendApduCommand,
            child: const Text('Send APDU Command'),
          ),
          SizedBox(height: 20),
      ]
      )
    );
  }
}
