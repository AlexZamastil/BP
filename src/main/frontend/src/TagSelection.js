import React, { useState } from 'react';

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

  return (
    <div>
      <h1>Tag Selection</h1> <br/>
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