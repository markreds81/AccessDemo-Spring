package it.markreds.accessdemo;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class DoorEditor extends VerticalLayout implements KeyNotifier {
    private final DoorRepository repository;
    private Door door;
    TextField displayName = new TextField("Display name");
    TextField macAddress = new TextField("MAC Address");
    IntegerField workTime = new IntegerField("Work time");
    Button saveButton = new Button("Save", VaadinIcon.CHECK.create());
    Button cancelButton = new Button("Cancel");
    Button deleteButton = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(saveButton, cancelButton, deleteButton);
    Binder<Door> binder = new Binder<>(Door.class);
    private ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public DoorEditor(DoorRepository repository) {
        this.repository = repository;

        add(displayName, macAddress, workTime, actions);
        binder.bindInstanceFields(this);

        setSpacing(true);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        addKeyPressListener(Key.ENTER, e -> save());
        saveButton.addClickListener(e -> save());
        deleteButton.addClickListener(e -> delete());
        cancelButton.addClickListener(e -> editDoor(door));
        workTime.setStepButtonsVisible(true);
        workTime.setMin(0);
        workTime.setMax(30);
        setVisible(false);
    }

    void save() {
        repository.save(door);
        changeHandler.onChange();
    }

    void delete() {
        repository.delete(door);
        changeHandler.onChange();
    }

    public final void editDoor(Door target) {
        if (target == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = target.getId() != null;
        if (persisted) {
            door = repository.findById(target.getId()).get();
        } else {
            door = target;
        }
        cancelButton.setVisible(persisted);
        binder.setBean(door);
        setVisible(true);
        displayName.focus();
    }

    public void setChangeHandler(ChangeHandler ch) {
        changeHandler = ch;
    }
}
