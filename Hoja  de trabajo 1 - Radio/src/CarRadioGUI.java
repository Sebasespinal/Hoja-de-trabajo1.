import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CarRadioGUI {
    private JFrame frame;
    private Radio radio;
    private JLabel stationLabel;
    private JLabel volumeLabel;
    private JLabel statusLabel;
    private Timer volumeUpTimer;
    private JMenuItem[] btnSaveStationMenuItems;

    public CarRadioGUI() {
        radio = new Radio();
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setTitle("Radio del Automóvil");

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        volumeLabel = new JLabel("Volumen: " + radio.getVolume());
        stationLabel = new JLabel("Estación actual: " + radio.getFormattedFrequency() + " - " + radio.getBand());
        statusLabel = new JLabel("Apagado");
        statusLabel.setForeground(Color.RED);
        infoPanel.add(volumeLabel);
        infoPanel.add(stationLabel);
        infoPanel.add(statusLabel);
        mainPanel.add(infoPanel, BorderLayout.NORTH);

        JPanel radioPanel = createRadioPanel();
        mainPanel.add(radioPanel, BorderLayout.CENTER);

        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }

    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        return button;
    }

    private JButton createButtonWithMouseAdapter(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (text.equals("+") || text.equals("-")) {
                    stopVolumeUp();
                } else {
                    int buttonIndex = Integer.parseInt(text) - 1;
                    radio.selectStation(buttonIndex + 1);
                    updateStationLabel();
                }
            }
        });

        return button;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu stationMenu = new JMenu("Emisoras Guardadas");
        btnSaveStationMenuItems = new JMenuItem[12];

        for (int i = 0; i < 12; i++) {
            final int buttonIndex = i;
            JMenuItem menuItem = new JMenuItem("Botón " + (i + 1));
            btnSaveStationMenuItems[i] = menuItem;

            menuItem.addActionListener(e -> {
                radio.selectStation(buttonIndex + 1);
                updateStationLabel();
            });

            stationMenu.add(menuItem);
        }

        menuBar.add(stationMenu);
        return menuBar;
    }

    private JPanel createRadioPanel() {
        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton btnTurnOn = createButton("Encender", e -> {
            radio.turnOn();
            updateStationLabel();
            updateVolumeLabel();
            updateStatusLabel("Encendido");
            JOptionPane.showMessageDialog(frame, "Radio Encendido", "Información", JOptionPane.INFORMATION_MESSAGE);
        });
        radioPanel.add(btnTurnOn, gbc);

        JButton btnToggleBand = createButton("Cambiar Banda", e -> {
            radio.toggleBand();
            updateStationLabel();
        });
        gbc.gridx = 1;
        radioPanel.add(btnToggleBand, gbc);

        JButton btnNextStation = createButton("Sintonizar siguiente", e -> {
            radio.nextStation();
            updateStationLabel();
        });
        gbc.gridx = 2;
        radioPanel.add(btnNextStation, gbc);

        JButton btnSaveStation = createButton("Guardar Emisora", e -> {
            int button = showSaveStationDialog();
            if (button != -1) {
                radio.saveStation(button);
            }
            updateStationLabel();
        });
        gbc.gridy = 1;
        gbc.gridx = 0;
        radioPanel.add(btnSaveStation, gbc);

        for (int i = 0; i < 12; i++) {
            final int buttonIndex = i;
            JButton btnSelectStation = createButtonWithMouseAdapter(Integer.toString(i + 1), e -> {
                radio.selectStation(buttonIndex + 1);
                updateStationLabel();
            });
            gbc.gridx = i % 3;
            gbc.gridy = i / 3 + 2;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            radioPanel.add(btnSelectStation, gbc);
        }

        JButton btnVolumeDown = createButton("Bajar Volumen", e -> {
            radio.decreaseVolume();
            updateVolumeLabel();
        });
        gbc.gridx = 0;
        gbc.gridy = 6;  // Ajusta la posición vertical
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        radioPanel.add(btnVolumeDown, gbc);

        JButton btnVolumeUp = createButton("Subir Volumen", e -> {
            startVolumeUp();
        });
        gbc.gridx = 1;
        gbc.gridy = 6;  // Ajusta la posición vertical
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        radioPanel.add(btnVolumeUp, gbc);

        JButton btnVolumeMute = createButton("Mute", e -> {
            radio.mute();
            updateVolumeLabel();
        });
        gbc.gridx = 2;
        gbc.gridy = 6;  // Ajusta la posición vertical
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        radioPanel.add(btnVolumeMute, gbc);

        JButton btnTurnOff = createButton("Apagar", e -> {
            radio.turnOff();
            updateStationLabel();
            updateVolumeLabel();
            updateStatusLabel("Apagado");
            JOptionPane.showMessageDialog(frame, "Radio Apagado", "Información", JOptionPane.INFORMATION_MESSAGE);
        });
        gbc.gridx = 3;
        gbc.gridy = 6;  // Ajusta la posición vertical
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        radioPanel.add(btnTurnOff, gbc);

        return radioPanel;
    }

    private void startVolumeUp() {
        radio.increaseVolume();
        updateVolumeLabel();
        updateStationLabel();
    }

   
    private void stopVolumeUp() {
        if (volumeUpTimer != null && volumeUpTimer.isRunning()) {
            volumeUpTimer.stop();
        }
    }

    private void updateStatusLabel(String status) {
        statusLabel.setText(status);
    }

    private void updateVolumeLabel() {
        volumeLabel.setText("Volumen: " + radio.getVolume());
    }
    private void updateStationLabel() {
        stationLabel.setText("Estación actual: " + radio.getFormattedFrequency() + " - " + radio.getBand());
    }
    private int showSaveStationDialog() {
        String[] buttons = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        int choice = JOptionPane.showOptionDialog(frame, "Selecciona el número para guardar la emisora:",
                "Guardar Emisora", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);

        return choice + 1; // Ajustar el índice del arreglo al rango de 1 a 12
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CarRadioGUI carRadioGUI = new CarRadioGUI();
            carRadioGUI.frame.setJMenuBar(carRadioGUI.createMenuBar());
        });
    }
}