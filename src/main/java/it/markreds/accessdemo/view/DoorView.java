package it.markreds.accessdemo.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import it.markreds.accessdemo.domain.Door;
import it.markreds.accessdemo.repository.DoorRepository;
import org.springframework.util.StringUtils;

@Route(value = "doors", layout = MainLayout.class)
@PageTitle("Doors | Access Demo")
public class DoorView extends VerticalLayout {
    private final DoorRepository repository;
    private final DoorEditor editor;
    private final Button addNewButton;
    final Grid<Door> grid;
    final TextField filter;

    public DoorView(DoorRepository repository, DoorEditor editor) {
        this.repository = repository;
        this.editor = editor;
        this.grid = new Grid<>(Door.class);
        this.filter = new TextField();
        this.addNewButton = new Button("New door", VaadinIcon.PLUS.create());

        buildLayout();
    }

    private void buildLayout() {
        HorizontalLayout actions = new HorizontalLayout(filter, addNewButton);
        add(actions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns("id", "displayName", "macAddress", "workTime");
        grid.getColumnByKey("id").setWidth("150px").setFlexGrow(0);

        filter.setPlaceholder("Filter by name");
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(event -> listDoors(event.getValue()));

        grid.asSingleSelect().addValueChangeListener(e -> editor.editDoor(e.getValue()));

        addNewButton.addClickListener(event -> editor.editDoor(new Door("")));

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listDoors(filter.getValue());
        });

        listDoors(null);
    }

    void listDoors(String filterText) {
        if (StringUtils.hasText(filterText)) {
            grid.setItems(repository.findByDisplayNameStartsWithIgnoreCase(filterText));
        } else {
            grid.setItems(repository.findAll());
        }
    }
}
