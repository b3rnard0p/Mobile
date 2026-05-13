import React, { useState } from 'react';
import {
  View,
  Text,
  TextInput,
  Button,
  ScrollView,
  StyleSheet,
  Alert
} from 'react-native';
import { RadioButton, Checkbox } from 'react-native-paper';

export default function App() {
  // 1. Estados para os campos de texto
  const [nome, setNome] = useState('');
  const [cpf, setCpf] = useState('');
  const [telefone, setTelefone] = useState('');
  const [endereco, setEndereco] = useState('');

  // 2. Estados para Seleção (Radio e Checkbox)
  const [sexo, setSexo] = useState('Masculino');
  const [aceitouTermos, setAceitouTermos] = useState(false);
  const [receberEmail, setReceberEmail] = useState(false);

  // 3. Lista e ID
  const [contatos, setContatos] = useState([]);
  const [proximoId, setProximoId] = useState(1);

  function adicionarContato() {
    // Validação obrigatória dos termos
    if (!aceitouTermos) {
      Alert.alert("Erro", "Você precisa aceitar os termos para continuar.");
      return;
    }

    if (nome.trim() === '' || cpf.trim() === '') {
      Alert.alert("Erro", "Nome e CPF são obrigatórios.");
      return;
    }

    // Criando o objeto completo
    const novo = {
      id: proximoId,
      nome,
      cpf,
      telefone,
      endereco,
      sexo,
      aceitouTermos: aceitouTermos ? "Aceitou termos" : "Não aceitou",
      receberEmail: receberEmail ? "Quer receber e-mail" : "Não quer receber e-mail"
    };

    setContatos(contatos.concat(novo));
    setProximoId(proximoId + 1);

    // Limpar campos após salvar
    setNome('');
    setCpf('');
    setTelefone('');
    setEndereco('');
    setAceitouTermos(false);
    setReceberEmail(false);
  }

  return (
    <View style={styles.container}>
      <Text style={styles.titulo}>Cadastro de Contatos</Text>

      <ScrollView showsVerticalScrollIndicator={false}>
        {/* Campos de Texto */}
        <TextInput style={styles.input} placeholder="Nome" value={nome} onChangeText={setNome} />
        <TextInput style={styles.input} placeholder="CPF" value={cpf} keyboardType="numeric" onChangeText={setCpf} />
        <TextInput style={styles.input} placeholder="Telefone" value={telefone} keyboardType="phone-pad" onChangeText={setTelefone} />
        <TextInput style={styles.input} placeholder="Endereço" value={endereco} onChangeText={setEndereco} />

        {/* Radio Button para Sexo */}
        <Text style={styles.label}>Sexo:</Text>
        <RadioButton.Group onValueChange={value => setSexo(value)} value={sexo}>
          <View style={styles.row}>
            <View style={styles.row}><RadioButton value="Masculino" /><Text>Masculino</Text></View>
            <View style={styles.row}><RadioButton value="Feminino" /><Text>Feminino</Text></View>
          </View>
        </RadioButton.Group>

        {/* Checkboxes */}
        <View style={styles.row}>
          <Checkbox status={aceitouTermos ? 'checked' : 'unchecked'} onPress={() => setAceitouTermos(!aceitouTermos)} />
          <Text>Aceitar Termos (obrigatório)</Text>
        </View>

        <View style={styles.row}>
          <Checkbox status={receberEmail ? 'checked' : 'unchecked'} onPress={() => setReceberEmail(!receberEmail)} />
          <Text>Receber informações por e-mail</Text>
        </View>

        <Button title="SALVAR" onPress={adicionarContato} />

        {/* Exibição da Lista */}
        <View style={styles.lista}>
          {contatos.map(c => (
            <View key={c.id} style={styles.card}>
              <Text style={styles.itemNome}>{c.id} - {c.nome}</Text>
              <Text>CPF: {c.cpf}</Text>
              <Text>Telefone: {c.telefone}</Text>
              <Text>Endereço: {c.endereco}</Text>
              <Text>Sexo: {c.sexo}</Text>
              <Text>{c.aceitouTermos}</Text>
              <Text>{c.receberEmail}</Text>
            </View>
          ))}
        </View>
      </ScrollView>
    </View>
  );
}

{/* Estilo */}
const styles = StyleSheet.create({
  container: { flex: 1, padding: 20, marginTop: 40, backgroundColor: '#fff' },
  titulo: { fontSize: 22, fontWeight: 'bold', marginBottom: 15 },
  input: { borderWidth: 1, borderColor: '#ddd', padding: 8, marginBottom: 10, borderRadius: 4 },
  label: { fontSize: 16, marginTop: 10 },
  row: { flexDirection: 'row', alignItems: 'center', marginVertical: 2 },
  lista: { marginTop: 20, borderTopWidth: 1, borderColor: '#eee' },
  card: { padding: 10, borderBottomWidth: 1, borderBottomColor: '#ccc', marginBottom: 10 },
  itemNome: { fontSize: 18, fontWeight: 'bold' }
});