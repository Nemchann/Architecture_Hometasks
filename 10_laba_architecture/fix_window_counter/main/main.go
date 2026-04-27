package main

import (
	"fmt"
	"sync"
	"time"
)

type FixedWindow struct {
	limit    int
	counter  int
	window   time.Duration
	lastTick int64
	mu       sync.Mutex
}

func (fw *FixedWindow) Allow() bool {
	fw.mu.Lock()
	defer fw.mu.Unlock()

	now := time.Now().UnixNano() / int64(fw.window)
	if now > fw.lastTick {
		fw.counter = 0
		fw.lastTick = now
	}

	if fw.counter < fw.limit {
		fw.counter++
		return true
	}
	return false
}

func main() {
	fw := &FixedWindow{limit: 3, window: 2 * time.Second}
	for i := 0; i < 7; i++ {
		fmt.Printf("Запрос %d: %v\n", i+1, fw.Allow())
		time.Sleep(400 * time.Millisecond)
	}
}	