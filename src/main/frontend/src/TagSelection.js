import React, { useState } from 'react';

/**
 * Author: Alex Zamastil
 * Component that is used for selecting tags.
 */

function TagSelection({pool, tags }) {
  const [selectedTags, setSelectedTags] = useState([]);

  const handleTagSelection = (tag) => {
    if (selectedTags.includes(tag)) {
      setSelectedTags(selectedTags.filter((item) => item !== tag));
    } else{
      setSelectedTags([...selectedTags, tag]);
    }
    tags = selectedTags;
    console.log(tags);
    console.log(selectedTags);
  };
//returns list of tags that could be selected and assigned as selected tags
  return (
    <div>
      <h1>{t('tag_selection')} </h1> <br/>
      <ul>
        {pool.map((tag) => (
          <li className='listStyleTypeNone' key={tag}>
            <label>
              <input
                type="checkbox"
                checked={selectedTags.includes(tag)}
                onChange={() => handleTagSelection(tag)}
              />
              {tag}
            </label>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default TagSelection;