
class Radio {
    private boolean isOn;
    private String band;
    private double frequency;
    private double[] savedStations;
    private int volume;
    private boolean isMuted;

    // Constructor
    public Radio() {
        setOn(false);
        band = "AM";
        frequency = 530.0;  // Inicial frecuencia AM
        savedStations = new double[12];  // Array para almacenar las emisoras guardadas
        volume = 50;  // Volumen inicial
        isMuted = false;
    }

    // Métodos para manipular el estado del radio
    public void turnOn() {
        setOn(true);
        System.out.println("Radio encendido");
    }

    public void turnOff() {
        setOn(false);
        System.out.println("Radio apagado");
    }

    public void toggleBand() {
        if (band.equals("AM")) {
            band = "FM";
            frequency = 87.9;  // Inicial frecuencia FM
        } else {
            band = "AM";
            frequency = 530.0;  // Inicial frecuencia AM
        }
        System.out.println("Cambiando a la banda: " + band);
    }

    public void nextStation() {
        if (band.equals("AM")) {
            frequency += 10;
            if (frequency > 1610) {
                frequency = 530;
            }
        } else {
            frequency += 0.2;
            if (frequency > 107.9) {
                frequency = 87.9;
            }
        }
        System.out.println("Sintonizando a: " + getFormattedFrequency() + " " + band);
    }

    public void saveStation(int button) {
        // Implementa la lógica para guardar la emisora actual en el botón especificado
        savedStations[button - 1] = frequency;
        System.out.println("Guardando emisora en el botón " + button);
    }

    public void selectStation(int button) {
        // Implementa la lógica para seleccionar la emisora almacenada en el botón especificado
        if (savedStations[button - 1] != 0.0) {
            frequency = savedStations[button - 1];
            System.out.println("Seleccionando emisora del botón " + button);
        } else {
            System.out.println("El botón " + button + " no tiene ninguna emisora guardada.");
        }
    }

    public void increaseVolume() {
        if (volume < 100) {
            volume++;
            System.out.println("Volumen aumentado a: " + volume);
        } else {
            System.out.println("El volumen ya está al máximo.");
        }
    }

    public void decreaseVolume() {
        if (volume > 0) {
            volume--;
            System.out.println("Volumen disminuido a: " + volume);
        } else {
            System.out.println("El volumen ya está al mínimo.");
        }
    }

    public void mute() {
        isMuted = !isMuted;
        if (isMuted) {
            System.out.println("Radio en modo silencio");
        } else {
            System.out.println("Modo silencio desactivado");
        }
    }

    public String getFormattedFrequency() {
        return String.format("%.1f", frequency);
    }

    public String getBand() {
        return band;
    }

    public int getVolume() {
        return isMuted ? 0 : volume;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean isOn) {
        this.isOn = isOn;
    }
}






