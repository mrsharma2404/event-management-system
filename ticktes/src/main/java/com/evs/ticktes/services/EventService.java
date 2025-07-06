package com.evs.ticktes.services;

import com.evs.ticktes.domain.CreateEventRequest;
import com.evs.ticktes.domain.entities.Event;

import java.util.UUID;

public interface EventService {
    Event createEvent(UUID organizerId, CreateEventRequest event);
}
