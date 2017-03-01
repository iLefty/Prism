/*
 * This file is part of Prism, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2015 Helion3 http://helion3.com/
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.helion3.prism.events.listeners;

import com.helion3.prism.api.records.PrismRecord;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.entity.spawn.EntitySpawnCause;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.DropItemEvent;

public class DropItemListener {
    /**
     * Saves event records when a player drops an item.
     *
     * @param event Dispense event.
     */
    @Listener(order = Order.POST)
    public void onDrop(final DropItemEvent.Dispense event, @Root EntitySpawnCause spawncause) {
        if (spawncause.getEntity() instanceof User) {
            for (Entity entity : event.getEntities()) {
                PrismRecord.create().entity(spawncause.getEntity()).dropped(entity).save();
            }
        }
    }
}