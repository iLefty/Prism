/**
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
package com.helion3.prism.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

import com.google.common.base.Optional;
import com.helion3.prism.Prism;
import com.helion3.prism.api.query.Query;
import com.helion3.prism.api.query.QuerySession;
import com.helion3.prism.api.results.Actionable;
import com.helion3.prism.api.results.ActionableResult;
import com.helion3.prism.api.results.ResultRecord;
import com.helion3.prism.utils.Format;
import com.helion3.prism.utils.Template;
import com.helion3.prism.utils.Translation;

public class RollbackCommand implements CommandCallable {

    @Override
    public Optional<CommandResult> process(CommandSource source, String arguments) throws CommandException {
        // @todo error out when no args. Currently, "lookup" is passed as the arg when no real args used

        final Query query = Query.fromParameters(arguments);
        query.setAggregate(false);
        final QuerySession session = new QuerySession(query);

        try {
            List<ActionableResult> actionResults = new ArrayList<ActionableResult>();
            // Iterate query results
            List<ResultRecord> results = Prism.getStorageAdapter().records().query(session);
            if (results.isEmpty()) {
                source.sendMessage(Format.error("No results."));
            } else {
                // Iterate record results
                for (ResultRecord result : results) {
                    if(result instanceof Actionable) {
                        Actionable actionable = (Actionable) result;
                        actionResults.add(actionable.undo());
                    }
                }

                // Iterate results from actionables
                if (!actionResults.isEmpty()) {
                    int appliedCount = 0;
                    int skippedCount = 0;

                    for (ActionableResult result : actionResults) {
                        if (result.applied()) {
                            appliedCount++;
                        } else {
                            skippedCount++;
                        }
                    }

                    Map<String,String> tokens = new HashMap<String, String>();
                    tokens.put("appliedCount", ""+appliedCount);
                    tokens.put("skippedCount", ""+skippedCount);

                    String messageTemplate = null;
                    if (skippedCount > 0) {
                        messageTemplate = Translation.from("rollback.success.withskipped");
                    } else {
                        messageTemplate = Translation.from("rollback.success");
                    }

                    source.sendMessage(Format.heading(
                        Texts.of(Template.parseTemplate(messageTemplate, tokens)),
                        " ", Format.bonus(Translation.from("rollback.success.bonus"))
                    ));
                }
            }
        } catch (Exception e) {
            // @todo handle
            e.printStackTrace();
        }

        return Optional.of(CommandResult.success());
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return true;
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return null;
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return null;
    }

    @Override
    public Text getUsage(CommandSource source) {
        return null;
    }
}