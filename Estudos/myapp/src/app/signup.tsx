import {View, StyleSheet, Image, Text, ScrollView, KeyboardAvoidingView, Platform} from "react-native"
import { Link } from "expo-router";
import { Input } from "../components/Input";
import { Button } from "../components/Button";

export default function Signup() {
    return (
        <KeyboardAvoidingView style={{flex: 1}} behavior={Platform.OS === "ios" ? "padding" : "height"}>
        <ScrollView contentContainerStyle={{flexGrow: 1}} keyboardShouldPersistTaps="handled" showsVerticalScrollIndicator={false}>
        <View style={styles.container}>
            <Image
                source={require("../assets/Pessoa2.jpg")}
                style={styles.ilustration}
            />
            <Text style={styles.title}>Cadastrar</Text>
            <Text style={styles.subtitle}>Crie sua conta com email e senha.</Text>
            <View style={styles.form}>
                <Input placeholder="Nome" keyboardType="default"/>
                <Input placeholder="E-mail" keyboardType="email-address"/>
                <Input placeholder="Senha" secureTextEntry={true} />
                <Input placeholder="Confirme a senha" secureTextEntry={true} />
                <Button label="Cadastrar" />
            </View>
            <Text style={styles.footerText}>Já tem uma conta? <Link href="/" style={styles.footerLink}>Entre aqui.</Link></Text>
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