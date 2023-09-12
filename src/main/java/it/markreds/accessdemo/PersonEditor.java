package it.markreds.accessdemo;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class PersonEditor extends VerticalLayout implements KeyNotifier {
    private final PersonRepository repository;

    private Person person;

    TextField lastName = new TextField("Last name");
    TextField firstName = new TextField("First name");
    TextField keyCode = new TextField("Key code");
    Checkbox enabled = new Checkbox("Enabled");
    Button saveButton = new Button("Save", VaadinIcon.CHECK.create());
    Button cancelButton = new Button("Cancel");
    Button deleteButton = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(saveButton, cancelButton, deleteButton);
    Binder<Person> binder = new Binder<>(Person.class);

    private ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public PersonEditor(PersonRepository repository) {
        this.repository = repository;

        add(firstName, lastName, keyCode, enabled, actions);
        binder.bindInstanceFields(this);
        setSpacing(true);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        addKeyPressListener(Key.ENTER, e -> save());
        saveButton.addClickListener(e -> save());
        deleteButton.addClickListener(e -> delete());
        cancelButton.addClickListener(e -> editPerson(person));
        setVisible(false);
    }

    void save() {
        repository.save(person);
        changeHandler.onChange();
    }

    void delete() {
        repository.delete(person);
        changeHandler.onChange();
    }

    public final void editPerson(Person target) {
        if (target == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = target.getId() != null;
        if (persisted) {
            person = repository.findById(target.getId()).get();
        } else {
            person = target;
        }
        cancelButton.setVisible(persisted);
        binder.setBean(person);
        setVisible(true);
        firstName.focus();
    }

    public void setChangeHandler(ChangeHandler ch) {
        changeHandler = ch;
    }
}
