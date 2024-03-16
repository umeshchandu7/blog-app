package com.mountblue.blogapp.Service;

import com.mountblue.blogapp.Entity.Tag;
import com.mountblue.blogapp.Repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> checkForTags(List<Tag> tags) {
        List<Tag> newTags = new ArrayList<>();
        for (Tag tag : tags) {
            Optional<Tag> tagName = tagRepository.findByName(tag.getName());
            if (!tagName.isEmpty()) {
                newTags.add(tagName.get());
            } else {
                newTags.add(tag);
            }
        }
        return newTags;
    }
}
