package com.ssin.todolist.ui.main.interactor;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ssin.todolist.model.Tag;
import com.ssin.todolist.model.Task;
import com.ssin.todolist.model.Taskable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SS-In on 2018-07-15.
 */

public class MainInteractorImpl implements MainInteractor {
    private DatabaseReference reference;
    private DatabaseReference tagsReference;
    private FirebaseAuth auth;
    private OnTaskAddListener onTaskAddListener;
    private TagsListener tagsListener;

    private ChildEventListener childEventListener;
    private ValueEventListener valueEventListener;
    private ValueEventListener singleValueEventListener;

    private ValueEventListener tagEventListener;

    public MainInteractorImpl(DatabaseReference reference, FirebaseAuth auth) {
        this.reference = reference.child(auth.getCurrentUser().getUid()).child("tasks");
        this.tagsReference = reference.child(auth.getCurrentUser().getUid()).child("tags");

        this.auth = auth;

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Task task = dataSnapshot.getValue(Task.class);
                task.setParent(dataSnapshot.getKey());
                onTaskAddListener.onNewTaskAdded(task);
                Log.d("INFO", "On child added: " + dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Taskable> taskList = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Task task = ds.getValue(Task.class);
                    task.setParent(ds.getKey());
                    taskList.add(task);
                }

                onTaskAddListener.onAllTaskFetched(taskList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        singleValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Task task = dataSnapshot.getValue(Task.class);
                task.setParent(dataSnapshot.getKey());
                onTaskAddListener.onTaskFetched(task);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        tagEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Tag> tags = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Tag tag = ds.getValue(Tag.class);
                    tags.add(tag);
                }
                tagsListener.onAllTagsFetched(tags);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @Override
    public void fetchAllTasks(OnTaskAddListener onTaskAddListener) {
        this.onTaskAddListener = onTaskAddListener;
        reference.removeEventListener(childEventListener);
        reference.removeEventListener(valueEventListener);
        reference.orderByChild("date").addChildEventListener(childEventListener);
        reference.orderByChild("date").addValueEventListener(valueEventListener);
    }

    @Override
    public void addNewTask(Task task) {
        reference.push().setValue(task);
    }

    @Override
    public void updateTask(Task task) {
        reference.child(task.getParent()).updateChildren(task.toMap());
    }

    @Override
    public void fetchAllTags(TagsListener tagsListener) {
        this.tagsListener = tagsListener;
        tagsReference.removeEventListener(tagEventListener);
        tagsReference.addValueEventListener(tagEventListener);
    }

    @Override
    public void addNewTag(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        tagsReference.child(name).setValue(tag);
    }

    @Override
    public void getTasksByTag(String tag) {
        reference.orderByChild("tags/" + tag + "/name").equalTo(tag).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Taskable> taskList = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Task task = ds.getValue(Task.class);
                    task.setParent(ds.getKey());
                    taskList.add(task);
                }

                onTaskAddListener.onAllTaskFetched(taskList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getTasksByDate(String date) {
        reference.orderByChild("date").equalTo(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Taskable> taskList = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Task task = ds.getValue(Task.class);
                    task.setParent(ds.getKey());
                    taskList.add(task);
                }

                onTaskAddListener.onAllTaskFetched(taskList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void logout(OnLogoutFinishListener listener) {
        auth.signOut();
        listener.onSuccess();
    }

    @Override
    public void getUserProfile(OnGetUserProfileFinishListener listener) {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            String displayname = user.getDisplayName();
            listener.onGetProfileFinished(email, displayname);
        }
    }

    @Override
    public void deleteTask(final Task task, final OnTaskDeleteFinishListener listener) {
        reference.child(task.getParent()).
                removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onTaskDeleted(task.getTitle());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onTaskDeletedError(task.getTitle());
            }
        });
    }

    @Override
    public void fetchUndoneTasks(final OnTaskAddListener listener) {
        reference.orderByChild("done").equalTo(false).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Taskable> taskList = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Task task = ds.getValue(Task.class);
                    task.setParent(ds.getKey());
                    taskList.add(task);
                }

                listener.onAllTaskFetched(taskList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void clearDoneTasks(final OnRemoveDoneTasksListener listener) {
        reference.orderByChild("done").equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                    reference.child(ds.getKey()).removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
