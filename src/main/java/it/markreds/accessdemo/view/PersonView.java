package it.markreds.accessdemo.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import it.markreds.accessdemo.domain.Person;
import it.markreds.accessdemo.repository.PersonRepository;
import it.markreds.accessdemo.view.MainLayout;
import it.markreds.accessdemo.view.PersonEditor;
import org.springframework.util.StringUtils;

import java.util.Comparator;

@Route(value = "people", layout = MainLayout.class)
@PageTitle("People | Access Demo")
public class PersonView extends VerticalLayout {
    private final PersonRepository repository;
    private final PersonEditor editor;
    private final Button addNewButton;
    final Grid<Person> grid;
    final TextField filter;

    public PersonView(PersonRepository repository, PersonEditor editor) {
        this.repository = repository;
        this.editor = editor;
        this.grid = new Grid<>(Person.class);
        this.filter = new TextField();
        this.addNewButton = new Button("New person", VaadinIcon.PLUS.create());

        buildLayout();
    }

    private void buildLayout() {
        HorizontalLayout actions = new HorizontalLayout(filter, addNewButton);
        add(actions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns("id", "firstName", "lastName", "keyCode");
        grid.getColumnByKey("id").setWidth("150px").setFlexGrow(0);
        grid.addComponentColumn((item) -> {
            Icon icon;
            if (item.isEnabled()) {
                icon = VaadinIcon.CHECK.create();
                icon.setColor("green");
            } else {
                icon = VaadinIcon.CLOSE.create();
                icon.setColor("red");
            }
            return icon;
        })
                .setKey("enabled")
                .setHeader("Enabled")
                .setComparator(Comparator.comparing(Person::isEnabled));

        filter.setPlaceholder("Filter by name");
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(event -> listPeople(event.getValue()));

        grid.asSingleSelect().addValueChangeListener(e -> editor.editPerson(e.getValue()));

        addNewButton.addClickListener(event -> editor.editPerson(new Person("", "")));

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listPeople(filter.getValue());
        });

        listPeople(null);
    }

    void listPeople(String filterText) {
        if (StringUtils.hasText(filterText)) {
            grid.setItems(repository.findByFirstNameStartsWithIgnoreCaseOrLastNameStartsWithIgnoreCase(filterText, filterText));
        } else {
            grid.setItems(repository.findAll());
        }
    }

}
