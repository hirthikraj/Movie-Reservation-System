package com.project.mrs.lock;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Getter
@Component
public class SeatLock {
    private final ConcurrentHashMap<Long, ReentrantLock> currentSeatsLocked = new ConcurrentHashMap<>();

    public ReentrantLock getShowSeatLock(Long showSeatId)
    {
        return currentSeatsLocked.computeIfAbsent(showSeatId, _ -> new ReentrantLock());
    }

    public void removeLockForShowSeat(long seatId){
        currentSeatsLocked.remove(seatId);
    }
}
