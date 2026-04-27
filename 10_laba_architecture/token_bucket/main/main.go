package main

import (
	"fmt"
	"sync"
	"time"
)

type TokenBucket struct {
	capacity  int
	tokens    int
	rate      time.Duration
	mu        sync.Mutex
}

func NewTokenBucket(capacity int, rate time.Duration) *TokenBucket {
	tb := &TokenBucket{
		capacity: capacity,
		tokens:   capacity,
		rate:     rate,
	}
	// Каждые 'rate' секунд добавляем один токен
	go func() {
		ticker := time.NewTicker(tb.rate)
		for range ticker.C {
			tb.mu.Lock()
			if tb.tokens < tb.capacity {
				tb.tokens++
			}
			tb.mu.Unlock()
		}
	}()
	return tb
}

func (tb *TokenBucket) Allow() bool {
	tb.mu.Lock()
	defer tb.mu.Unlock()
	if tb.tokens > 0 {
		tb.tokens--
		return true
	}
	return false
}

func main() {
	bucket := NewTokenBucket(5, 500*time.Millisecond) // Емкость 5, +1 токен каждые 0.5с
	for i := 0; i < 10; i++ {
		fmt.Printf("Запрос %d: %v\n", i+1, bucket.Allow())
		time.Sleep(200 * time.Millisecond)
	}
}