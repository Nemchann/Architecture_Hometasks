package main

import (
	"fmt"
	"time"
)

type LeakyBucket struct {
	queue chan struct{}
}

func NewLeakyBucket(capacity int, leakRate time.Duration) *LeakyBucket {
	lb := &LeakyBucket{
		queue: make(chan struct{}, capacity),
	}
	// "Вытекание": извлекаем данные из канала с постоянной скоростью
	go func() {
		ticker := time.NewTicker(leakRate)
		for range ticker.C {
			select {
			case <-lb.queue:
			default:
			}
		}
	}()
	return lb
}

func (lb *LeakyBucket) Allow() bool {
	select {
	case lb.queue <- struct{}{}: // Пытаемся добавить "каплю" в ведро
		return true
	default:
		return false // Ведро переполнено
	}
}

func main() {
	lb := NewLeakyBucket(3, 1*time.Second) // Емкость 3, вытекает 1 раз в секунду
	for i := 0; i < 6; i++ {
		fmt.Printf("Запрос %d: %v\n", i+1, lb.Allow())
		time.Sleep(200 * time.Millisecond)
	}
}