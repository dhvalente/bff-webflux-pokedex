package br.com.webflux.pokedex.model;

import java.util.Objects;

public class PokemonEvent {
    private long eventId;

    private String eventType;

    public PokemonEvent(long eventId, String eventType) {
        this.eventId = eventId;
        this.eventType = eventType;
    }

    public PokemonEvent() {
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "PokemonEvent{" +
                "eventId=" + eventId +
                ", eventType='" + eventType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PokemonEvent that = (PokemonEvent) o;
        return eventId == that.eventId && Objects.equals(eventType, that.eventType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, eventType);
    }
}
