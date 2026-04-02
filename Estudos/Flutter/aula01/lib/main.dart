import 'package:flutter/material.dart';
import './exemplo01_stateless.dart';
import './exemplo02_stateful.dart';
import './exemplo03_stateful.dart'; 
import './exemplo04_cadastro.dart';
import './exemplo05_listar.dart';

void main() {
  runApp(const MyApp());
}

// Widget principal do app
class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      initialRoute: '/', // Rota inicial
      routes: {
        '/': (context) => const PrimeiraTela(), // Primeira tela
        '/segunda': (context) => const SegundaTela(), // Segunda tela
        '/formulario': (context) => const FormularioBasicoPage(), // Tela do formulário
        '/cadastro': (context) => const CadastroClientePage(),
        '/listar': (context) => const ListarClientesPage(),
      },
    );
  }
}