import com.github.lx200916.uploadtopastemeintellij.listeners.OnPublicDialogListener;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Pattern;

public class PublicPasteDialog extends JDialog {
    private final String lang;
    private JPanel contentPane;
    private JButton cancelButton;
    private JButton OKButton;
    private JTextField PasteID;
    private JComboBox PasteLang;
    private JCheckBox burnAfterRead;
    private JTextArea PasteContent;

    public PublicPasteDialog(String pasteLang, final String pasteContent, final OnPublicDialogListener listener) {
        setContentPane(contentPane);
        lang = pasteLang;
        setModal(true);
        setTitle("Create Public Paste");
        setSize(600, 600);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setPasteContent(pasteContent);
        setPasteLang(pasteLang);
//        TextPrompt tp7 = new TextPrompt("First Name", tf7);

        PasteID.setText("You can specify PasteID only in a Burn After Read Paste.");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                listener.onCancelClicked(PublicPasteDialog.this);
            }
        });


        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onCancelClicked(PublicPasteDialog.this);
            }
        });

        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onCancelClicked(PublicPasteDialog.this);
            }
        });
        OKButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (burnAfterRead.isSelected() && getPasteID().getText().length() != 0) {
                    if (Pattern.matches("^[0-9a-z]{3,8}$", getPasteID().getText())) {
                        listener.onOkClicked(PublicPasteDialog.this);
                    } else {
                        Messages.showMessageDialog("Sorry, PasteID should be 8-16 long with digits and letters.", "Error", Messages.getErrorIcon());
                        return;
                    }

                } else {
                    listener.onOkClicked(PublicPasteDialog.this);

                }
            }
        });
        burnAfterRead.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                Boolean state = checkBox.getModel().isSelected();
                getPasteID().setEnabled(state);
                getPasteID().setText("");


            }
        });
    }

    public JTextField getPasteID() {
        return PasteID;
    }

//    public void setPasteID(String pasteID) {
//        setPasteID(pasteID);
//    }

    public String getPasteLang() {
        if (PasteLang.getSelectedItem() == null) {
            return lang;
        } else {
            System.out.println(PasteLang.getSelectedItem().toString());
//            Messages.showMessageDialog(PasteLang.getSelectedItem().toString(), "Error", Messages.getErrorIcon());

            return PasteLang.getSelectedItem().toString();

        }
    }

    public void setPasteLang(String pasteLang) {
//        PasteLang = pasteLang;
        PasteLang.getModel().setSelectedItem(pasteLang);
    }

    public Boolean getBurnAfterRead() {
        return burnAfterRead.isSelected();
    }

    public void setBurnAfterRead(Boolean burn) {
//        this.burnAfterRead = burnAfterRead;
        burnAfterRead.setSelected(burn);
    }

    public String getPasteContent() {
        return PasteContent.getText();
    }

    public void setPasteContent(String pasteContent) {
//        PasteContent = pasteContent;
        PasteContent.setText(pasteContent);

    }

    public void invalidPasteid() {
        com.intellij.openapi.ui.Messages.showMessageDialog("Oops..PasteID has been Taken..", "PasteID Taken", new ImageIcon("/icon/ohno.gif"));

    }
}
