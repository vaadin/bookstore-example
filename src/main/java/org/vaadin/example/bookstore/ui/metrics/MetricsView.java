package org.vaadin.example.bookstore.ui.metrics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.security.PermitAll;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import io.micrometer.core.instrument.Measurement;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;

import org.vaadin.example.bookstore.ui.MainLayout;

/**
 * Shows the Vaadin Flow runtime metrics collected by the
 * {@code vaadin-micrometer-spring-boot} instrumentation.
 * <p>
 * The view reads every {@code vaadin.*} meter from the application
 * {@link MeterRegistry} and renders one row per measurement. Use the refresh
 * button to re-sample the current values.
 */
@Route(value = "Metrics", layout = MainLayout.class)
@PageTitle("Metrics")
@PermitAll
public class MetricsView extends VerticalLayout {

    public static final String VIEW_NAME = "Metrics";

    private static final String METRIC_PREFIX = "vaadin.";

    private final transient MeterRegistry meterRegistry;
    private final Grid<MetricRow> grid = new Grid<>(MetricRow.class, false);

    public MetricsView(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        setSizeFull();

        final Button refresh = new Button("Refresh", VaadinIcon.REFRESH.create(),
                event -> refresh());
        refresh.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        final HorizontalLayout header = new HorizontalLayout(
                new H2("Vaadin runtime metrics"), refresh);
        header.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        header.setWidthFull();

        grid.addColumn(MetricRow::name).setHeader("Metric").setFlexGrow(2)
                .setSortable(true);
        grid.addColumn(MetricRow::tags).setHeader("Tags").setFlexGrow(3);
        grid.addColumn(MetricRow::statistic).setHeader("Statistic")
                .setFlexGrow(1);
        grid.addColumn(MetricRow::value).setHeader("Value").setFlexGrow(1);
        grid.setSizeFull();

        add(header, grid);

        refresh();
    }

    private void refresh() {
        final List<MetricRow> rows = new ArrayList<>();
        for (Meter meter : meterRegistry.getMeters()) {
            final String name = meter.getId().getName();
            if (!name.startsWith(METRIC_PREFIX)) {
                continue;
            }
            final String tags = meter.getId().getTags().stream()
                    .map(tag -> tag.getKey() + "=" + tag.getValue())
                    .collect(Collectors.joining(", "));
            for (Measurement measurement : meter.measure()) {
                rows.add(new MetricRow(name, tags,
                        measurement.getStatistic().name().toLowerCase(),
                        formatValue(measurement.getValue())));
            }
        }
        rows.sort(Comparator.comparing(MetricRow::name)
                .thenComparing(MetricRow::tags));
        grid.setItems(rows);

        if (rows.isEmpty()) {
            grid.setItems(new MetricRow(
                    "No vaadin.* metrics recorded yet", "", "", ""));
        }
    }

    private static String formatValue(double value) {
        if (value == Math.rint(value) && !Double.isInfinite(value)) {
            return Long.toString((long) value);
        }
        return String.format("%.3f", value);
    }

    /** A single measurement of a meter, flattened for grid display. */
    public record MetricRow(String name, String tags, String statistic,
            String value) {
    }
}
