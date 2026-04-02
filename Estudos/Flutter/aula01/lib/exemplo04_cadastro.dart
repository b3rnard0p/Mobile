import 'package:flutter/material.dart';

class CadastroClientePage extends StatelessWidget {
  const CadastroClientePage({super.key});

  @override
  Widget build(BuildContext context) {
    final _formKey = GlobalKey<FormState>();

    return Scaffold( 
      appBar: AppBar(
        title: const Text('Cadastro de Cliente'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0), 
        child: Form(
          key: _formKey, 
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start, 
            children: [
              TextFormField(
                decoration: const InputDecoration(labelText: 'Nome'),
                validator: (value) {
                  if (value == null || value.isEmpty) return 'Insira o nome';
                  return null;
                },
              ),
              const SizedBox(height: 10),
              TextFormField(
                decoration: const InputDecoration(labelText: 'CPF'),
              ),
              const SizedBox(height: 10),
              TextFormField(
                decoration: const InputDecoration(labelText: 'Telefone'),
              ),
              const SizedBox(height: 20), 
              
              // Botão de Salvar
              ElevatedButton(
                onPressed: () {
                  if (_formKey.currentState!.validate()) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text('Cliente salvo com sucesso!')),
                    );
                  }
                },
                child: const Text('Salvar'),
              ),
              const SizedBox(height: 10),

              // Botão para ir para a lista
              ElevatedButton(
                onPressed: () {
                   Navigator.pushNamed(context, '/listar'); 
                },
                child: const Text('Ir para Listar Clientes'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}