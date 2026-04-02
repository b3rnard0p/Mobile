import 'package:flutter/material.dart';

class ListarClientesPage extends StatelessWidget {
  const ListarClientesPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Listar Clientes'),
      ),
      body: const Center(
        // Futuramente você vai trocar esse Text por um ListView!
        child: Text(
          'A lista de clientes aparecerá aqui!',
          style: TextStyle(fontSize: 18),
        ),
      ),
    );
  }
}