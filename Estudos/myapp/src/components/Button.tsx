import {
    StyleSheet,
    Text,
    TouchableOpacity,
    TouchableOpacityProps
} from 'react-native';

type ButtonProps = TouchableOpacityProps & {
    label: string;
}

export function Button({...rest}: ButtonProps) {
    return (
        <TouchableOpacity style={styles.container} {...rest} activeOpacity={0.7}>
            <Text style={styles.label}>{rest.label}</Text>
        </TouchableOpacity>
    )
}

const styles = StyleSheet.create({
    container: {
        width: '100%',
        height: 48,
        backgroundColor: '#3366FF',
        alignItems: 'center',
        justifyContent: 'center',
        borderRadius: 8,
    },
    label: {
        color: '#FFFFFF',
        fontSize: 16,
        fontWeight: '600',
    }
});