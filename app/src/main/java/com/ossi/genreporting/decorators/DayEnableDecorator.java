package com.ossi.genreporting.decorators;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

public class DayEnableDecorator implements DayViewDecorator {
    @Override
    public boolean shouldDecorate(final CalendarDay day) {
        CalendarDay date= CalendarDay.today();
        return (day.isBefore(date)) ? true : false;
    }
    @Override
    public void decorate(final DayViewFacade view) {
        view.setDaysDisabled(true);
    }
}
