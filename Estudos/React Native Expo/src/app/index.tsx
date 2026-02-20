import {View, StyleSheet, Image, Text, ScrollView, KeyboardAvoidingView, Platform, Alert} from "react-native"
import { Link } from "expo-router";
import { Input } from "../components/Input";
import { Button } from "../components/Button";
import { useState } from "react";

export default function Index() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    function handleSignIn() {
        if(!email.trim() || !password.trim()) {
        Alert.alert("Erro ao entrar", "Preencha o email e a senha para continuar.");
        }
        Alert.alert("Bem-Vindo", `Login com ${email} realizado com sucesso!`);
    }
    return (
        <KeyboardAvoidingView style={{flex: 1}} behavior={Platform.OS === "ios" ? "padding" : "height"}>
        <ScrollView contentContainerStyle={{flexGrow: 1}} keyboardShouldPersistTaps="handled" showsVerticalScrollIndicator={false}>
        <View style={styles.container}>
            <Image
                source={require("../assets/Pessoa1.webp")}
                style={styles.ilustration}
            />
            <Text style={styles.title}>Entrar</Text>
            <Text style={styles.subtitle}>Acesse sua conta com email e senha.</Text>
            <View style={styles.form}>
                <Input placeholder="E-mail" keyboardType="email-address" onChangeText={setEmail}/>
                <Input placeholder="Senha" secureTextEntry={true} onChangeText={setPassword}/>
                <Button label="Entrar" onPress={handleSignIn}/>
            </View>
            <Text style={styles.footerText}>Não tem uma conta? <Link href="/signup" style={styles.footerLink}>Cadastre-se aqui.</Link></Text>
        </View>
        </ScrollView>
        </KeyboardAvoidingView>
    );
}

const styles = StyleSheet.create({
    container: {
        flex:1,
        backgroundColor: "#FDFDFD",
        padding: 32,
    },
    ilustration: {
        width: "100%",
        height: 330,
        resizeMode: "contain",
        marginTop: 62,
    },
    title: {
        fontSize: 32,
        fontWeight: 900,
    },
    subtitle: {
        fontSize: 16,
    },
    form: {
        marginTop: 24,
        gap: 12,
    },
    footerText: {
        marginTop: 24,
        textAlign: "center",
        color: "#585860",
    },
    footerLink: {
        color: "#3366FF",
        fontWeight: "700",
    }
});