package it.markreds.accessdemo;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.PollEvent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.*;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard | Access Demo")
public class MainView extends VerticalLayout {
    private static final Logger LOG = LoggerFactory.getLogger(MainView.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private final EventLogFilter filter = new EventLogFilter();
    private final EventLogRepository repository;
    private long count = 0;
    private ConfigurableFilterDataProvider<EventLog, Void, EventLogFilter> filterDataProvider;
    final Grid<EventLog> grid;
    final TextField searchField;

    public MainView(EventLogRepository repository) {
        this.repository = repository;
        this.grid = new Grid<>(EventLog.class);
        this.searchField = new TextField("");

        buildLayout();
    }

    private void buildLayout() {
        grid.setHeight("500px");
        grid.setColumns("id", "timestamp", "itemType", "door", "person");
        grid.getColumnByKey("id").setWidth("150px").setFlexGrow(0);
        grid.getColumnByKey("timestamp").setRenderer(new ComponentRenderer<>(item -> new Text(FORMATTER.format(item.getTimestamp().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()))));
        grid.getColumnByKey("door").setRenderer(new ComponentRenderer<>(item -> new Text(item.hasDoor() ? item.getDoor().getDisplayName() : null)));
        grid.getColumnByKey("person").setRenderer(new ComponentRenderer<>(item -> new Text(item.hasPerson() ? item.getPerson().getFullName() : null)));

        searchField.setWidth("50%");
        searchField.setPlaceholder("Search");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> {
            filter.setSearchTerm(e.getValue());
            filterDataProvider.setFilter(filter);
        });

        VerticalLayout layout = new VerticalLayout(searchField, grid);
        layout.setPadding(false);
        add(layout);

        listEvents();

        UI.getCurrent().setPollInterval(1000);
        UI.getCurrent().addPollListener((ComponentEventListener<PollEvent>) event -> {
            if (repository.count() > count) {
                listEvents();
            }
        });
    }

    void listEvents() {
        List<EventLog> all = repository.findAll();
        EventLogDataProvider dataProvider = new EventLogDataProvider(all);
        filterDataProvider = dataProvider.withConfigurableFilter();
        grid.setItems(filterDataProvider);
        grid.scrollToEnd();
        count = all.size();
    }

    private static final class EventLogDataProvider extends AbstractBackEndDataProvider<EventLog, EventLogFilter> {
        private final List<EventLog> database;

        public EventLogDataProvider(List<EventLog> items) {
            this.database = new ArrayList<>(items);
        }
        @Override
        protected Stream<EventLog> fetchFromBackEnd(Query<EventLog, EventLogFilter> query) {
            Stream<EventLog> stream = database.stream();

            if (query.getFilter().isPresent()) {
                stream = stream.filter(eventLog -> query.getFilter().get().test(eventLog));
            }

            if (!query.getSortOrders().isEmpty()) {
                stream = stream.sorted(sortComparator(query.getSortOrders()));
            }

            return stream.skip(query.getOffset()).limit(query.getLimit());
        }

        @Override
        protected int sizeInBackEnd(Query<EventLog, EventLogFilter> query) {
            return (int) fetchFromBackEnd(query).count();
        }

        private static Comparator<EventLog> sortComparator(List<QuerySortOrder> sortOrders) {
            return sortOrders.stream().map(sortOrder -> {
                Comparator<EventLog> comparator = eventLogFieldComparator(sortOrder.getSorted());
                if (sortOrder.getDirection() == SortDirection.DESCENDING) {
                    comparator = comparator.reversed();
                }
                return  comparator;
            }).reduce(Comparator::thenComparing).orElse((p1, p2) -> 0);
        }

        private static Comparator<EventLog> eventLogFieldComparator(String sorted) {
            if (sorted.equals("id")) {
                return Comparator.comparing(EventLog::getId);
            } else if (sorted.equals("timestamp")) {
                return Comparator.comparing(EventLog::getTimestamp);
            }
            return (p1, p2) -> 0;
        }
    }

    private static final class EventLogFilter {
        private String searchTerm;

        public void setSearchTerm(String searchTerm) {
            this.searchTerm = searchTerm;
        }

        public boolean test(EventLog log) {
            boolean matchesItemType = matches(log.getItemType().toString(), searchTerm);
            boolean matchesDoor = log.hasDoor() && matches(log.getDoor().getDisplayName(), searchTerm);
            boolean matchesPerson = log.hasPerson() && matches(log.getPerson().getFullName(), searchTerm);
            return matchesItemType || matchesDoor || matchesPerson;
        }

        private boolean matches(String value, String searchTerm) {
            return searchTerm == null || searchTerm.isEmpty() || value.toLowerCase().contains(searchTerm.toLowerCase());
        }
    }
}
